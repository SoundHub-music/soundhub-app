package com.soundhub.ui.messenger

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.soundhub.R
import com.soundhub.data.datastore.UserCredsStore
import com.soundhub.data.datastore.UserPreferences
import com.soundhub.data.enums.ApiStatus
import com.soundhub.data.model.Chat
import com.soundhub.data.model.Message
import com.soundhub.data.model.User
import com.soundhub.data.repository.ChatRepository
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
    uiStateDispatcher: UiStateDispatcher,
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

    fun updateUnreadMessageCount() = viewModelScope.launch(Dispatchers.IO) {
        combine(messengerUiState.map { it.chats }, uiState.map { it.authorizedUser }) { chats, user ->
            chats to user
        }.collect { (chats, user) ->
            val unreadMessageCount = chats.flatMap { it.messages }
                .count { !it.isRead && it.sender?.id != user?.id }

            _messengerUiState.update {
                it.copy(unreadMessagesCount = unreadMessageCount)
            }
        }
    }

    fun filterChats(chats: List<Chat>, searchBarText: String, authorizedUser: User?): List<Chat> {
        return if (searchBarText.isNotEmpty()) {
            chats.filter { chat ->
                val otherUser = chat.participants.firstOrNull { it.id != authorizedUser?.id }
                otherUser != null && SearchUtils.compareWithUsername(otherUser, searchBarText)
            }
        } else {
            chats
        }
    }

    suspend fun prepareMessagePreview(context: Context, lastMessage: Message): String {
        val authorizedUser: User? = uiState.map { it.authorizedUser }.firstOrNull()
        var lastMessageContent = lastMessage.content

        if (lastMessageContent.length > 20) {
            lastMessageContent = "${lastMessageContent.substring(0, 20)}..."
        }

        val prefix = context.getString(R.string.messenger_screen_last_message_prefix)
        lastMessageContent = if (lastMessage.sender?.id == authorizedUser?.id) {
            prefix + lastMessageContent
        } else "${lastMessage.sender?.firstName}: $lastMessageContent".trim()

        return lastMessageContent
    }

    suspend fun loadChats() {
        combine(userCreds, uiState.map { it.authorizedUser }) {
            creds, user -> creds to user
        }
            .firstOrNull { (_, user) -> user != null }
            ?.let { (creds, user) ->
                chatRepository.getAllChatsByUserId(creds.accessToken, user!!.id)
                    .onSuccess { response ->
                        val chats = response.body.orEmpty()
                        _messengerUiState.update {
                            it.copy(
                                chats = chats,
                                unreadMessagesCount = chats.flatMap { chats -> chats.messages }
                                    .count { msg -> !msg.isRead && msg.sender?.id != user.id },
                                status = ApiStatus.SUCCESS
                            )
                        }
                    }
                    .onFailure {
                        _messengerUiState.update {
                            it.copy(status = ApiStatus.ERROR)
                        }
                    }
            }
    }
}
