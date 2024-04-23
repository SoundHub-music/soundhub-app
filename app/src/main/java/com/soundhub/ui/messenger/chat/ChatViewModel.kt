package com.soundhub.ui.messenger.chat

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.soundhub.data.api.responses.HttpResult
import com.soundhub.data.datastore.UserCredsStore
import com.soundhub.data.datastore.UserPreferences
import com.soundhub.data.enums.ApiStatus
import com.soundhub.data.model.Chat
import com.soundhub.data.model.Message
import com.soundhub.data.model.User
import com.soundhub.data.repository.ChatRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(
    userCredsStore: UserCredsStore,
    private val chatRepository: ChatRepository
): ViewModel() {
    private val userCreds: Flow<UserPreferences> = userCredsStore.getCreds()
    var chatUiState: MutableStateFlow<ChatUiState> = MutableStateFlow(ChatUiState())
        private set

    fun setInterlocutor(user: User?) = chatUiState.update {
        it.copy(interlocutor = user)
    }

    fun loadChat(chatId: UUID) = viewModelScope.launch(Dispatchers.IO) {
        getChatById(chatId).firstOrNull()
            ?.onSuccess { response ->
                chatUiState.update {
                    val messages: List<Message> = response.body?.messages ?: emptyList()
                    val unreadMessageCount = messages.count { msg -> !msg.isRead }

                    it.copy(
                        chat = response.body,
                        unreadMessageCount = unreadMessageCount,
                        status = ApiStatus.SUCCESS
                    )
                }
            }
            ?.onFailure {
                chatUiState.update { it.copy(status = ApiStatus.ERROR) }
            }
    }

    fun getChatById(id: UUID): Flow<HttpResult<Chat?>> = flow {
        userCreds.collect { creds ->
            val response = chatRepository.getChatById(
                accessToken = creds.accessToken,
                chatId = id
            )
            emit(response)
        }
    }

    fun sendMessage(message: Message) {
        chatUiState.update {
            val chat = it.chat
            chat?.messages = chat?.messages?.plus(message)!!

            it.copy(chat = chat)
        }
        Log.d("ChatViewModel", "sendMessage[sent message]: $message")
        Log.d("ChatViewModel", "sendMessage[messages state]: ${chatUiState.value}")
    }
}