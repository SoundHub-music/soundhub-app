package com.soundhub.ui.messenger

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.soundhub.data.dao.UserDao
import com.soundhub.data.database.AppDatabase
import com.soundhub.data.datastore.UserCredsStore
import com.soundhub.data.datastore.UserPreferences
import com.soundhub.data.enums.ApiStatus
import com.soundhub.data.model.Chat
import com.soundhub.data.model.User
import com.soundhub.data.repository.ChatRepository
import com.soundhub.utils.SearchUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MessengerViewModel @Inject constructor(
    private val chatRepository: ChatRepository,
    userCredsStore: UserCredsStore,
    appDb: AppDatabase
): ViewModel() {
    private val userCreds: Flow<UserPreferences> = userCredsStore.getCreds()
    private val userDao: UserDao = appDb.userDao()

    val messengerUiState = MutableStateFlow(MessengerUiState())

    init {
        viewModelScope.launch {
            updateUserData()
            loadChats()
        }
    }

    private suspend fun updateUserData() {
        messengerUiState.update { it.copy(authorizedUser = userDao.getCurrentUser()) }
    }

    fun filterChats(
        chats: List<Chat>,
        searchBarText: String,
        authorizedUser: User?
    ): List<Chat> =
        if (searchBarText.isNotEmpty())
            chats.filter {
                val otherUser: User = it.participants
                    .first { user -> user.id != authorizedUser?.id }
                SearchUtils.compareWithUsername(otherUser, searchBarText)
            }
        else chats

    fun loadChats() = viewModelScope.launch {
        userCreds.firstOrNull()
            ?.accessToken
            ?.let { accessToken ->
            chatRepository.getAllChatsByCurrentUser(accessToken)
            .onSuccess { response ->
                val chats: List<Chat>? = response.body
                val unreadMessagesCountTotal: Int = chats
                    ?.flatMap { it.messages }
                    ?.count { !it.isRead } ?: 0

                messengerUiState.update {
                    it.copy(
                        chats = response.body ?: emptyList(),
                        unreadMessagesTotal = unreadMessagesCountTotal,
                        status = ApiStatus.SUCCESS
                    )
                }
            }
            .onFailure {
                messengerUiState.update { it.copy(
                    status = ApiStatus.ERROR
                ) }
            }
        }
    }
}