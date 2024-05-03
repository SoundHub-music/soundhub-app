package com.soundhub.ui.messenger.chat

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.soundhub.R
import com.soundhub.Route
import com.soundhub.data.api.responses.HttpResult
import com.soundhub.data.dao.UserDao
import com.soundhub.data.database.AppDatabase
import com.soundhub.data.datastore.UserCredsStore
import com.soundhub.data.datastore.UserPreferences
import com.soundhub.data.enums.ApiStatus
import com.soundhub.data.model.Chat
import com.soundhub.data.model.Message
import com.soundhub.data.model.User
import com.soundhub.data.repository.ChatRepository
import com.soundhub.ui.events.UiEvent
import com.soundhub.ui.viewmodels.UiStateDispatcher
import com.soundhub.utils.UiText
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
    private val chatRepository: ChatRepository,
    private val uiStateDispatcher: UiStateDispatcher,
    appDb: AppDatabase,
    userCredsStore: UserCredsStore
): ViewModel() {
    private val userCreds: Flow<UserPreferences> = userCredsStore.getCreds()
    private val userDao: UserDao = appDb.userDao()
    val chatUiState: MutableStateFlow<ChatUiState> = MutableStateFlow(ChatUiState())

    fun setInterlocutor(user: User?) = chatUiState.update {
        it.copy(interlocutor = user)
    }

    fun loadChatById(chatId: UUID) = viewModelScope.launch(Dispatchers.IO) {
        getChatById(chatId).firstOrNull()
            ?.onSuccess { response ->
                chatUiState.update {
                    val currentUser: User? = userDao.getCurrentUser()
                    val messages: List<Message> = response.body?.messages ?: emptyList()
                    val unreadMessageCount = messages.count { msg -> !msg.isRead }
                    val interlocutor: User? = response.body
                        ?.participants?.find { user -> user.id != currentUser?.id }

                    it.copy(
                        chat = response.body,
                        unreadMessageCount = unreadMessageCount,
                        interlocutor = interlocutor,
                        status = ApiStatus.SUCCESS
                    )
                }
            }
            ?.onFailure {
                chatUiState.update { it.copy(status = ApiStatus.ERROR) }
            }
    }

    private fun getChatById(id: UUID): Flow<HttpResult<Chat?>> = flow {
        userCreds.collect { creds ->
            val response = chatRepository.getChatById(
                accessToken = creds.accessToken,
                chatId = id
            )
            emit(response)
        }
    }

    fun deleteChat(chatId: UUID) = viewModelScope.launch(Dispatchers.IO) {
        var uiEvent: UiEvent
        userCreds.collect { creds ->
            chatRepository.deleteChatById(
                accessToken = creds.accessToken,
                chatId = chatId
            )
            .onSuccess {
                uiEvent = UiEvent.Navigate(Route.Messenger)
                uiStateDispatcher.sendUiEvent(uiEvent)
            }
            .onFailure {
                uiEvent = UiEvent.ShowToast(UiText.StringResource(R.string.toast_common_error))
                uiStateDispatcher.sendUiEvent(uiEvent)
            }
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