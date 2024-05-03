package com.soundhub.ui.friends

import android.util.Log
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
import com.soundhub.ui.events.UiEvent
import com.soundhub.ui.friends.enums.FriendListPage
import com.soundhub.ui.viewmodels.UiStateDispatcher
import com.soundhub.utils.SearchUtils
import com.soundhub.utils.UiText
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
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
    userCredsStore: UserCredsStore,
    appDb: AppDatabase
): ViewModel() {
    private val userCreds: Flow<UserPreferences> = userCredsStore.getCreds()
    private val userDao: UserDao = appDb.userDao()
    val friendsUiState: MutableStateFlow<FriendsUiState> = MutableStateFlow(FriendsUiState())

    val tabs: List<FriendListPage> = listOf(
        FriendListPage.MAIN,
        FriendListPage.RECOMMENDATIONS
    )

    init {
        viewModelScope.launch {
            val currentUser: User? = userDao.getCurrentUser()
            if (friendsUiState.value.recommendedFriends.isEmpty())
                loadRecommendedFriends(currentUser?.id)
        }
    }

    private fun loadRecommendedFriends(userId: UUID?) = viewModelScope.launch(Dispatchers.IO) {
        friendsUiState.update { it.copy(status = ApiStatus.LOADING) }
        userCreds.collect { creds ->
            userRepository.getRecommendedFriends(
                accessToken = creds.accessToken,
                userId = userId
            ).onSuccess { response ->
                friendsUiState.update {
                    it.copy(
                        recommendedFriends = response.body ?: emptyList(),
                        status = ApiStatus.SUCCESS
                    )
                }
            }.onFailure { error ->
                Log.d("FriendsViewModel", "error: $error")
                error.errorBody.detail?.let {
                    uiStateDispatcher.sendUiEvent(UiEvent.ShowToast(
                        UiText.DynamicString(it)
                    ))
                }
                friendsUiState.update { it.copy(status = ApiStatus.ERROR) }
            }
        }
    }

    fun getOrCreateChat(interlocutor: User): Flow<Chat?> = flow {
        userCreds.collect { creds ->
            val chat: Chat? = getOrCreateChatByUserUseCase(
                interlocutor = interlocutor,
                accessToken = creds.accessToken
            )
            emit(chat)
        }
    }

    fun filterFriendsList(occurrenceString: String, friends: List<User>): List<User> {
        return if (occurrenceString.isNotEmpty())
            friends.filter{ SearchUtils.compareWithUsername(it, occurrenceString) }
        else friends
    }
}