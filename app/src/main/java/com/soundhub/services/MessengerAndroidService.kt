package com.soundhub.services

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import com.google.gson.GsonBuilder
import com.soundhub.data.api.responses.HttpResult
import com.soundhub.data.dao.UserDao
import com.soundhub.data.datastore.UserCredsStore
import com.soundhub.data.datastore.UserPreferences
import com.soundhub.data.enums.ApiStatus
import com.soundhub.data.model.Chat
import com.soundhub.data.model.Message
import com.soundhub.data.model.User
import com.soundhub.data.repository.ChatRepository
import com.soundhub.data.websocket.WebSocketClient
import com.soundhub.ui.activities.MainActivity
import com.soundhub.utils.ApiEndpoints
import com.soundhub.utils.constants.Constants
import com.soundhub.utils.converters.json.LocalDateTimeAdapter
import com.soundhub.utils.converters.json.LocalDateWebSocketAdapter
import com.soundhub.utils.mappers.MessageMapper
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ua.naiksoftware.stomp.dto.StompMessage
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.UUID
import javax.inject.Inject

@AndroidEntryPoint
class MessengerAndroidService: Service() {
    @Inject
    lateinit var userCredsStore: UserCredsStore
    @Inject
    lateinit var userDao: UserDao
    @Inject
    lateinit var chatRepository: ChatRepository

    private lateinit var webSocketClient: WebSocketClient
    private val userCreds: Flow<UserPreferences> = userCredsStore.getCreds()

    private val coroutineScope = CoroutineScope(Dispatchers.IO + Job())

    private val _messengerUiState = MutableStateFlow(MessengerUiState())
    val messengerUiState = _messengerUiState.asStateFlow()

    private val _chatUiState = MutableStateFlow(ChatUiState())
    val chatUiState = _chatUiState.asStateFlow()

    private val gson = GsonBuilder()
        .registerTypeAdapter(LocalDateTime::class.java, LocalDateTimeAdapter())
        .registerTypeAdapter(LocalDate::class.java, LocalDateWebSocketAdapter())
        .create()

    override fun onBind(intent: Intent?): IBinder? {
        Log.i("MessengerAndroidService", "onBind: service was binded")
        TODO("Not yet implemented")
    }

    override fun onCreate() {
        super.onCreate()
        Log.i("MessengerAndroidService", "onCreate: service was created")

        coroutineScope.launch {
            initializeWebSocket(userCreds.firstOrNull()?.accessToken)
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.i(
            "MessengerAndroidService",
            "onStartCommand: service was started with commands -> flags: $flags, startId: $startId"
        )
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.i("MessengerAndroidService", "onDestroy: service was destroyed")
        webSocketClient.disconnect()
    }

    override fun startForegroundService(service: Intent?): ComponentName? {
        val channelId = "messenger_service_channel"
        val channelName = "Messenger Service Channel"

        val notificationChannel = NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_DEFAULT)
        val notificationManager: NotificationManager = getSystemService(Context.NOTIFICATION_SERVICE)
            as NotificationManager

        notificationManager.createNotificationChannel(notificationChannel)

        val notificationIntent = Intent(this, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            this,
            0,
            notificationIntent,
            PendingIntent.FLAG_IMMUTABLE
        )

        val notification: Notification = NotificationCompat.Builder(this, channelId)
            .setContentTitle("Chat Service")
            .setContentText("Running")
            .setContentIntent(pendingIntent)
            .build()

        startForeground(1, notification)
        return startService(service)
    }


    private fun initializeWebSocket(accessToken: String?) {
        webSocketClient = WebSocketClient(accessToken)
        webSocketClient.apply {
            connect(Constants.SOUNDHUB_WEBSOCKET)

            subscribe(
                topic = ApiEndpoints.ChatWebSocket.WS_GET_MESSAGES_TOPIC,
                messageListener = ::onReceiveMessageListener,
                errorListener = ::onSubscribeErrorListener
            )
            subscribe(
                topic = ApiEndpoints.ChatWebSocket.WS_READ_MESSAGE_TOPIC,
                messageListener = ::onReadMessageListener,
                errorListener = ::onSubscribeErrorListener
            )
            subscribe(
                topic = ApiEndpoints.ChatWebSocket.WS_DELETE_MESSAGE_TOPIC,
                messageListener = ::onDeleteMessageListener,
                errorListener = ::onSubscribeErrorListener
            )
        }
    }

    fun sendMessage(message: String) = coroutineScope.launch {
        val authorizedUser: User? = userDao.getCurrentUser()
        val (chat, _, _ ) = _chatUiState.value
        val sendMessageRequest = MessageMapper.impl.toSendMessageRequest(
            chat = chat,
            userId = authorizedUser?.id,
            content = message
        )
        val messageObj = Message(sender = authorizedUser, content = message)

        webSocketClient.sendMessage(sendMessageRequest, onComplete = {
            _chatUiState.update {
                val updatedMessages = it.chat?.messages.orEmpty() + messageObj
                it.copy(chat = it.chat?.copy(messages = updatedMessages))
            }
        })
    }


    fun readMessage(message: Message) = webSocketClient.readMessage(message.id)

    fun deleteMessage(message: Message) = webSocketClient.deleteMessage(message.id)

    fun onSubscribeErrorListener(error: Throwable) {
        Log.e("ChatViewModel", "onSubscribeErrorListener: $error")
    }

    fun onReceiveMessageListener(message: StompMessage) {
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

    fun onReadMessageListener(message: StompMessage) {
        Log.i("ChatViewModel", "Read message: $message")
        val readMessage: Message = gson.fromJson(message.payload, Message::class.java)
        _chatUiState.update {
            val updatedMessages = it.chat?.messages.orEmpty().map { msg ->
                if (msg.id == readMessage.id) readMessage else msg
            }
            it.copy(chat = it.chat?.copy(messages = updatedMessages))
        }
    }

    fun onDeleteMessageListener(message: StompMessage) {
        Log.i("ChatViewModel", "Deleted message: $message")
        val deletedMessage: Message = gson.fromJson(message.payload, Message::class.java)
        _chatUiState.update {
            val updatedMessages = it.chat?.messages.orEmpty().filter { msg -> msg.id != deletedMessage.id }
            it.copy(chat = it.chat?.copy(messages = updatedMessages))
        }
    }

    fun loadChatById(chatId: UUID) = coroutineScope.launch() {
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

    fun loadChats() = coroutineScope.launch {
        val authorizedUser: Flow<User?> = flow { emit(userDao.getCurrentUser()) }
        combine(userCreds, authorizedUser) {
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
                                unreadMessagesCountTotal = chats.flatMap { chats -> chats.messages }
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


data class MessengerUiState(
    val chats: List<Chat> = emptyList(),
    val unreadMessagesCountTotal: Int = 0,
    val status: ApiStatus = ApiStatus.LOADING
)

data class ChatUiState(
    val chat: Chat? = null,
    val interlocutor: User? = null,
    val unreadMessageCount: Int = 0,
    val status: ApiStatus = ApiStatus.LOADING,
)