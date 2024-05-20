package com.soundhub.ui.messenger.chat

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.GsonBuilder
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
import com.soundhub.data.websocket.WebSocketClient
import com.soundhub.ui.events.UiEvent
import com.soundhub.ui.states.UiState
import com.soundhub.ui.viewmodels.UiStateDispatcher
import com.soundhub.utils.ApiEndpoints.ChatWebSocket.WS_DELETE_MESSAGE_TOPIC
import com.soundhub.utils.ApiEndpoints.ChatWebSocket.WS_GET_MESSAGES_TOPIC
import com.soundhub.utils.ApiEndpoints.ChatWebSocket.WS_READ_MESSAGE_TOPIC
import com.soundhub.utils.UiText
import com.soundhub.utils.constants.Constants.SOUNDHUB_WEBSOCKET
import com.soundhub.utils.converters.json.LocalDateTimeAdapter
import com.soundhub.utils.converters.json.LocalDateWebSocketAdapter
import com.soundhub.utils.mappers.MessageMapper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import ua.naiksoftware.stomp.dto.StompMessage
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val chatRepository: ChatRepository,
    private val uiStateDispatcher: UiStateDispatcher,
    appDb: AppDatabase,
    userCredsStore: UserCredsStore
) : ViewModel() {
    private val webSocketClient: WebSocketClient? = uiStateDispatcher.uiState.value.webSocketClient
    private val userCreds: Flow<UserPreferences> = userCredsStore.getCreds()
    private val userDao: UserDao = appDb.userDao()
    private val uiState: Flow<UiState> = uiStateDispatcher.uiState

    private val _chatUiState: MutableStateFlow<ChatUiState> = MutableStateFlow(ChatUiState())
    val chatUiState: StateFlow<ChatUiState> = _chatUiState.asStateFlow()

    private val gson = GsonBuilder()
        .registerTypeAdapter(LocalDateTime::class.java, LocalDateTimeAdapter())
        .registerTypeAdapter(LocalDate::class.java, LocalDateWebSocketAdapter())
        .create()

    init {
        viewModelScope.launch {
            initializeWebSocket(userCreds.firstOrNull()?.accessToken)
        }
    }

    override fun onCleared() {
        super.onCleared()
        Log.d("ChatViewModel", "ViewModel was cleared")
    }

    private fun initializeWebSocket(accessToken: String?) {
        uiStateDispatcher.setWebSocketClient(WebSocketClient(accessToken))
        webSocketClient?.apply {
            connect(SOUNDHUB_WEBSOCKET)

            subscribe(
                topic = WS_GET_MESSAGES_TOPIC,
                messageListener = ::onReceiveMessageListener,
                errorListener = ::onSubscribeErrorListener
            )
            subscribe(
                topic = WS_READ_MESSAGE_TOPIC,
                messageListener = ::onReadMessageListener,
                errorListener = ::onSubscribeErrorListener
            )
            subscribe(
                topic = WS_DELETE_MESSAGE_TOPIC,
                messageListener = ::onDeleteMessageListener,
                errorListener = ::onSubscribeErrorListener
            )
        }
    }


    private fun onSubscribeErrorListener(error: Throwable) {
        Log.e("ChatViewModel", "onSubscribeErrorListener: $error")
    }

    private fun onReceiveMessageListener(message: StompMessage) {
        try {
            Log.i("ChatViewModel", "Received message: $message")
            val receivedMessage: Message = gson.fromJson(message.payload, Message::class.java)
            _chatUiState.update {
                val updatedMessages = it.chat?.messages.orEmpty() + receivedMessage
                it.copy(chat = it.chat?.copy(messages = updatedMessages))
            }
        } catch (e: Exception) {
            Log.e("ChatViewModel", "WebSocket error: ${e.stackTraceToString()}")
        }
    }

    private fun onReadMessageListener(message: StompMessage) {
        Log.i("ChatViewModel", "Read message: $message")
        val readMessage: Message = gson.fromJson(message.payload, Message::class.java)
        _chatUiState.update {
            val updatedMessages = it.chat?.messages.orEmpty().map { msg ->
                if (msg.id == readMessage.id) readMessage else msg
            }
            it.copy(chat = it.chat?.copy(messages = updatedMessages))
        }
    }

    private fun onDeleteMessageListener(message: StompMessage) {
        Log.i("ChatViewModel", "Deleted message: $message")
        val deletedMessage: Message = gson.fromJson(message.payload, Message::class.java)
        _chatUiState.update {
            val updatedMessages = it.chat?.messages.orEmpty().filter { msg -> msg.id != deletedMessage.id }
            it.copy(chat = it.chat?.copy(messages = updatedMessages))
        }
    }

    fun loadChatById(chatId: UUID) = viewModelScope.launch(Dispatchers.IO) {
        getChatById(chatId).firstOrNull()
            ?.onSuccess { response ->
                val currentUser = userDao.getCurrentUser()
                val messages = response.body?.messages.orEmpty()
                val unreadMessageCount = messages.count { it.sender?.id != currentUser?.id && !it.isRead }
                val interlocutor = response.body?.participants?.find { it.id != currentUser?.id }

                _chatUiState.update {
                    it.copy(
                        chat = response.body,
                        unreadMessageCount = unreadMessageCount,
                        interlocutor = interlocutor,
                        status = ApiStatus.SUCCESS
                    )
                }
            }
            ?.onFailure {
                _chatUiState.update { it.copy(status = ApiStatus.ERROR) }
            }
    }

    private fun getChatById(id: UUID): Flow<HttpResult<Chat?>> = flow {
        userCreds.collect { creds ->
            emit(chatRepository.getChatById(creds.accessToken, id))
        }
    }

    fun deleteChat(chatId: UUID) = viewModelScope.launch(Dispatchers.IO) {
        val creds = userCreds.firstOrNull()
        chatRepository.deleteChatById(creds?.accessToken, chatId)
            .onSuccess  {
                uiStateDispatcher.sendUiEvent(UiEvent.Navigate(Route.Messenger))
            }
            .onFailure {
                val toastMessage = UiText.StringResource(R.string.toast_common_error)
                uiStateDispatcher.sendUiEvent(UiEvent.ShowToast(toastMessage))
            }
    }

    fun setMessageContent(message: String) = _chatUiState.update { it.copy(messageContent = message) }

    fun sendMessage() = viewModelScope.launch(Dispatchers.IO) {
        val authorizedUser: User? = uiState.map { it.authorizedUser }.firstOrNull()
        val (chat, messageContent) = _chatUiState.value
        val sendMessageRequest = MessageMapper.impl.toSendMessageRequest(
            chat = chat,
            userId = authorizedUser?.id,
            content = messageContent
        )
        val message = Message(sender = authorizedUser, content = messageContent)

        webSocketClient?.sendMessage(sendMessageRequest) {
            _chatUiState.update {
                val updatedMessages = it.chat?.messages.orEmpty() + message
                it.copy(messageContent = "", chat = it.chat?.copy(messages = updatedMessages))
            }
        }
    }

    fun readVisibleMessages(messageIndex: Int) = viewModelScope.launch(Dispatchers.IO) {
        val authorizedUser: User? = uiState.map { it.authorizedUser }.firstOrNull()
        authorizedUser?.let {  user ->
            val messages: List<Message> = chatUiState.map { it.chat?.messages }
                .firstOrNull()
                .orEmpty()

            val visibleInterlocutorMessages: List<Message> = messages.subList(messageIndex, messages.size)
                .filter { it.sender?.id != user.id && !it.isRead }

            visibleInterlocutorMessages.forEach { msg -> readMessage(msg)}
            Log.d("ChatScreen", "visible message: ${visibleInterlocutorMessages.size}")
        }
    }

    private fun readMessage(message: Message) = webSocketClient?.readMessage(message.id)

    fun deleteMessage(message: Message) = webSocketClient?.deleteMessage(message.id)
}
