package com.soundhub.ui.messenger

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.soundhub.data.dao.UserDao
import com.soundhub.data.datastore.UserCredsStore
import com.soundhub.data.datastore.UserPreferences
import com.soundhub.data.enums.ApiStatus
import com.soundhub.data.model.Chat
import com.soundhub.data.model.Message
import com.soundhub.data.model.User
import com.soundhub.data.repository.ChatRepository
import com.soundhub.ui.events.UiEvent
import com.soundhub.ui.viewmodels.UiStateDispatcher
import com.soundhub.utils.SearchUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MessengerViewModel @Inject constructor(
    private val chatRepository: ChatRepository,
    private val uiStateDispatcher: UiStateDispatcher,
    private val userDao: UserDao,
    userCredsStore: UserCredsStore,
): ViewModel() {
    private val uiState = uiStateDispatcher.uiState
    val userCreds: Flow<UserPreferences> = userCredsStore.getCreds()

    private val _messengerUiState = MutableStateFlow(MessengerUiState())
    val messengerUiState = _messengerUiState.asStateFlow()

    init {
        viewModelScope.launch { loadChats() }
    }

    override fun onCleared() {
        super.onCleared()
        Log.d("MessengerViewModel", "viewmodel was cleared")
        _messengerUiState.update { MessengerUiState() }
    }

    fun updateUnreadMessageCount(cachedChatList: List<Chat> = emptyList()) = viewModelScope.launch(Dispatchers.IO) {
        val authorizedUser: User? = userDao.getCurrentUser()
        val chats = cachedChatList.ifEmpty { getChats().firstOrNull().orEmpty() }
        val unreadMessageCount = chats.flatMap { it.messages }
            .count { !it.isRead && it.sender?.id != authorizedUser?.id }

        _messengerUiState.update {
            it.copy(unreadMessagesCount = unreadMessageCount)
        }
    }

    fun filterChats(chats: List<Chat>, searchBarText: String, authorizedUser: User?): List<Chat> {
        return if (searchBarText.isNotEmpty()) {
            chats.filter { chat ->
                val otherUser = chat.participants.firstOrNull { it.id != authorizedUser?.id }
                otherUser != null && SearchUtils.compareWithUsername(otherUser, searchBarText)
            }
        } else chats
    }

    suspend fun prepareMessagePreview(prefix: String, lastMessage: Message): String {
        val authorizedUser: User? = uiState.map { it.authorizedUser }.firstOrNull()
        var lastMessageContent = lastMessage.content

        if (lastMessageContent.length > 20) {
            lastMessageContent = "${lastMessageContent.substring(0, 20)}..."
        }

        lastMessageContent = if (lastMessage.sender?.id == authorizedUser?.id) {
            prefix + lastMessageContent
        } else "${lastMessage.sender?.firstName}: $lastMessageContent".trim()

        return lastMessageContent
    }

    suspend fun loadChats() {
        val chats = getChats()
            .firstOrNull()
            .orEmpty()

        _messengerUiState.update {
            it.copy(
                chats = chats,
                status = ApiStatus.SUCCESS
            )
        }

        updateUnreadMessageCount(chats)
    }

    private fun getChats(): Flow<List<Chat>> = flow {
        val user = userDao.getCurrentUser()
        user?.let {
            chatRepository.getAllChatsByUserId(user.id)
                .onSuccess { response ->
                    val chats = response.body.orEmpty()
                    emit(chats)
                }
                .onFailure { error ->
                    emit(emptyList())
                    val errorEvent: UiEvent = UiEvent.Error(error.errorBody, error.throwable)
                    uiStateDispatcher.sendUiEvent(errorEvent)
                    _messengerUiState.update {
                        it.copy(status = ApiStatus.ERROR)
                    }
                }
        }
    }
}
