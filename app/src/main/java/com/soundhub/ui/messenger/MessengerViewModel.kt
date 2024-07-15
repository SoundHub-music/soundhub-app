package com.soundhub.ui.messenger

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.soundhub.Route
import com.soundhub.data.dao.UserDao
import com.soundhub.data.datastore.UserCredsStore
import com.soundhub.data.datastore.model.UserPreferences
import com.soundhub.data.enums.ApiStatus
import com.soundhub.data.model.Chat
import com.soundhub.data.model.Message
import com.soundhub.data.model.User
import com.soundhub.data.repository.ChatRepository
import com.soundhub.data.states.MessengerUiState
import com.soundhub.ui.events.UiEvent
import com.soundhub.ui.viewmodels.UiStateDispatcher
import com.soundhub.utils.SearchUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import java.util.UUID
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

    override fun onCleared() {
        super.onCleared()
        Log.d("MessengerViewModel", "viewmodel was cleared")
        _messengerUiState.update { MessengerUiState() }
    }

    suspend fun updateUnreadMessageCount(
        cachedChatList: List<Chat> = emptyList()
    ) = viewModelScope.launch(Dispatchers.IO) {
        val authorizedUser: User? = userDao.getCurrentUser()
        val chats = cachedChatList.ifEmpty { getChats().firstOrNull().orEmpty() }
        val unreadMessageCount = chats.count {
            chat -> chat.messages.any {
                m -> !m.isRead && m.sender?.id != authorizedUser?.id
            }
        }

        withContext(Dispatchers.Main) {
            _messengerUiState.update {
                it.copy(unreadMessagesCount = unreadMessageCount)
            }
        }
    }

    fun onChatCardClick(chat: Chat) = viewModelScope.launch(Dispatchers.Main) {
        val route = Route.Messenger.Chat.getRouteWithNavArg(chat.id.toString())
        uiStateDispatcher.sendUiEvent(UiEvent.Navigate(route))
    }

    fun getUnreadMessageCountByChatId(chatId: UUID?): UInt {
        val authorizedUser: User? = runBlocking { userDao.getCurrentUser() }
        val ( chats: List<Chat> ) = _messengerUiState.value

        return chats.find { chat -> chat.id == chatId}
            ?.messages
            ?.count { message ->
                !message.isRead && message.sender?.id != authorizedUser?.id
            }?.toUInt() ?: 0u
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
            "$prefix $lastMessageContent"
        } else "${lastMessage.sender?.firstName}: $lastMessageContent".trim()

        return lastMessageContent
    }

    fun loadChats() = viewModelScope.launch(Dispatchers.IO) {
        getChats().collect { chats ->
            withContext(Dispatchers.Main) {
                _messengerUiState.update {
                    it.copy(chats = chats)
                }
            }
        }
    }

    private fun getChats(): Flow<List<Chat>> = flow {
        userDao.getCurrentUser()?.let { user ->
            chatRepository.getAllChatsByUserId(user.id)
                .onSuccess { response ->
                    val chats = response.body.orEmpty()
                    _messengerUiState.update {
                        it.copy(status = ApiStatus.SUCCESS)
                    }
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
