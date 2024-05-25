package com.soundhub.ui.friends

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.soundhub.data.dao.UserDao
import com.soundhub.data.database.AppDatabase
import com.soundhub.data.datastore.UserCredsStore
import com.soundhub.data.datastore.UserPreferences
import com.soundhub.data.enums.ApiStatus
import com.soundhub.data.model.Chat
import com.soundhub.data.model.User
import com.soundhub.data.repository.UserRepository
import com.soundhub.domain.usecases.chat.GetOrCreateChatByUserUseCase
import com.soundhub.domain.usecases.user.GetUserByIdUseCase
import com.soundhub.ui.events.UiEvent
import com.soundhub.ui.friends.enums.FriendListPage
import com.soundhub.ui.viewmodels.UiStateDispatcher
import com.soundhub.utils.SearchUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class FriendsViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val uiStateDispatcher: UiStateDispatcher,
    private val getOrCreateChatByUserUseCase: GetOrCreateChatByUserUseCase,
    private val getUserByIdUseCase: GetUserByIdUseCase,
    appDb: AppDatabase,
    userCredsStore: UserCredsStore
): ViewModel() {
    private val userCreds: Flow<UserPreferences> = userCredsStore.getCreds()
    private var searchUsersJob: Job? = null
    private val userDao: UserDao = appDb.userDao()
    private val authorizedUser: MutableStateFlow<User?> = MutableStateFlow(null)

    val friendsUiState: MutableStateFlow<FriendsUiState> = MutableStateFlow(FriendsUiState())
    val tabs: List<FriendListPage> = listOf(
        FriendListPage.MAIN,
        FriendListPage.RECOMMENDATIONS,
        FriendListPage.SEARCH
    )

    init { viewModelScope.launch { initializeFriendsUiState() } }

    private suspend fun initializeFriendsUiState() = authorizedUser.update {
        userDao.getCurrentUser()
    }

    fun loadRecommendedFriends() = viewModelScope.launch(Dispatchers.IO) {
        friendsUiState.update { it.copy(status = ApiStatus.LOADING) }
        userCreds.collect { creds ->
            userRepository.getRecommendedFriends(creds.accessToken)
                .onSuccess { response ->
                friendsUiState.update {
                    it.copy(
                        recommendedFriends = response.body.orEmpty(),
                        status = ApiStatus.SUCCESS
                    )
                }
            }.onFailure { error ->
                val errorEvent: UiEvent = UiEvent.Error(error.errorBody, error.throwable)
                uiStateDispatcher.sendUiEvent(errorEvent)
                friendsUiState.update { it.copy(status = ApiStatus.ERROR) }
            }
        }
    }

    fun getOrCreateChat(interlocutor: User): Flow<Chat?> = flow {
        userCreds.collect { creds ->
            authorizedUser.value?.let {  user ->
                val chat: Chat? = getOrCreateChatByUserUseCase(
                    interlocutor = interlocutor,
                    accessToken = creds.accessToken,
                    userId = user.id
                )
                emit(chat)

            } ?: emit(null)
        }
    }

    fun filterFriendsList(occurrenceString: String, users: List<User>): List<User> {
        return if (occurrenceString.isNotEmpty() && occurrenceString.isNotBlank())
            users.filter { SearchUtils.compareWithUsername(it, occurrenceString) }
        else users
    }

    fun searchUsers(username: String) {
        searchUsersJob?.cancel()

        if (username.isEmpty() || username.isBlank()) {
            friendsUiState.update { it.copy(foundUsers = emptyList()) }
            return
        }

        searchUsersJob = viewModelScope.launch(Dispatchers.IO) {
             userRepository.searchUserByFullName(
                 accessToken = userCreds.firstOrNull()?.accessToken,
                 name = username
             ).onSuccess { response ->
                 val otherUsers: List<User> = response.body
                     ?.filter { user -> user.id != authorizedUser.firstOrNull()?.id }
                     .orEmpty()

                 friendsUiState.update {
                     it.copy(
                         foundUsers = otherUsers,
                         status = ApiStatus.SUCCESS
                     )
                 }
             }
             .onFailure { error ->
                 val errorEvent: UiEvent = UiEvent.Error(error.errorBody, error.throwable)
                 uiStateDispatcher.sendUiEvent(errorEvent)
                 friendsUiState.update { it.copy(
                     status = ApiStatus.ERROR,
                     foundUsers = emptyList()
                 ) }
             }
        }
    }

    private fun setProfileOwner(user: User?) = viewModelScope.launch(Dispatchers.IO) {
        friendsUiState.update {
            it.copy(profileOwner = user)
        }
    }

    fun loadProfileOwner(id: UUID) = viewModelScope.launch(Dispatchers.IO) {
        val authorizedUser: User? = authorizedUser.firstOrNull()
        val profileOwner: User? = if (authorizedUser?.id == id)
            authorizedUser
        else getUserByIdUseCase(id)

        setProfileOwner(profileOwner)
    }
}