package com.soundhub.ui.pages.friends

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.soundhub.Route
import com.soundhub.data.dao.UserDao
import com.soundhub.data.enums.ApiStatus
import com.soundhub.data.model.Chat
import com.soundhub.data.model.User
import com.soundhub.data.repository.UserRepository
import com.soundhub.data.states.FriendsUiState
import com.soundhub.domain.usecases.chat.GetOrCreateChatByUserUseCase
import com.soundhub.domain.usecases.user.GetUserByIdUseCase
import com.soundhub.ui.events.UiEvent
import com.soundhub.ui.pages.friends.enums.FriendListPage
import com.soundhub.ui.viewmodels.UiStateDispatcher
import com.soundhub.utils.lib.SearchUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
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
    private val userDao: UserDao,
): ViewModel() {
    private var searchUsersJob: Job? = null
    private val authorizedUser = MutableStateFlow<User?>(null)

    private val _friendsUiState = MutableStateFlow(FriendsUiState())
    val friendsUiState: StateFlow<FriendsUiState> = _friendsUiState.asStateFlow()
    val tabs: List<FriendListPage> = listOf(
        FriendListPage.MAIN,
        FriendListPage.RECOMMENDATIONS,
        FriendListPage.SEARCH
    )

    init { viewModelScope.launch { initializeFriendsUiState() } }

    private suspend fun initializeFriendsUiState() = authorizedUser.update {
        userDao.getCurrentUser()
    }

    fun onNavigateToChatBtnClick(user: User) = viewModelScope.launch(Dispatchers.Main) {
        getOrCreateChat(user)
            .firstOrNull()
            ?.let {
                val route: Route = Route.Messenger.Chat
                    .getRouteWithNavArg(it.id.toString())

                uiStateDispatcher.sendUiEvent(UiEvent.Navigate(route))
            }
    }

    fun loadRecommendedFriends() = viewModelScope.launch(Dispatchers.IO) {
        _friendsUiState.update { it.copy(status = ApiStatus.LOADING) }
        userRepository.getRecommendedFriends()
            .onSuccess { response ->
                _friendsUiState.update {
                    it.copy(
                        recommendedFriends = response.body.orEmpty(),
                        status = ApiStatus.SUCCESS
                    )
                }
        }.onFailure { error ->
            val errorEvent: UiEvent = UiEvent.Error(error.errorBody, error.throwable)
            uiStateDispatcher.sendUiEvent(errorEvent)
            _friendsUiState.update { it.copy(status = ApiStatus.ERROR) }
        }
    }

    private fun getOrCreateChat(interlocutor: User): Flow<Chat?> = flow {
        authorizedUser.value?.let { user ->
            val chat: Chat? = getOrCreateChatByUserUseCase(
                interlocutor = interlocutor,
                userId = user.id
            )
            emit(chat)

        } ?: emit(null)
    }

    fun filterFriendsList(occurrenceString: String, users: List<User>): List<User> {
        return if (occurrenceString.isNotEmpty() && occurrenceString.isNotBlank())
            users.filter { SearchUtils.compareWithUsername(it, occurrenceString) }
        else users
    }

    fun searchUsers(username: String) {
        searchUsersJob?.cancel()

        if (username.isEmpty() || username.isBlank()) {
            _friendsUiState.update { it.copy(foundUsers = emptyList()) }
            return
        }

        searchUsersJob = viewModelScope.launch(Dispatchers.IO) {
             userRepository.searchUserByFullName(username)
                 .onSuccess { response ->
                 val otherUsers: List<User> = response.body
                     ?.filter { user -> user.id != authorizedUser.firstOrNull()?.id }
                     .orEmpty()

                 _friendsUiState.update {
                     it.copy(
                         foundUsers = otherUsers,
                         status = ApiStatus.SUCCESS
                     )
                 }
             }
             .onFailure { error ->
                 val errorEvent: UiEvent = UiEvent.Error(error.errorBody, error.throwable)
                 uiStateDispatcher.sendUiEvent(errorEvent)
                 _friendsUiState.update { it.copy(
                     status = ApiStatus.ERROR,
                     foundUsers = emptyList()
                 ) }
             }
        }
    }

    private fun setProfileOwner(user: User?) = viewModelScope.launch(Dispatchers.IO) {
        _friendsUiState.update {
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

    fun loadUsersCompatibility(userIds: List<UUID>) = viewModelScope.launch(Dispatchers.IO) {
        userRepository.getUsersCompatibilityPercentage(userIds)
            .onSuccess { response ->
                _friendsUiState.update {
                    it.copy(
                        userCompatibilityPercentage = response.body
                    )
                }
            }
            .onFailure { error ->
                val uiEvent: UiEvent = UiEvent.Error(error.errorBody, error.throwable)
                 uiStateDispatcher.sendUiEvent(uiEvent)
            }
    }
}