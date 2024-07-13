package com.soundhub.services

import android.Manifest
import android.app.Service
import android.content.Intent
import android.content.pm.PackageManager
import android.os.IBinder
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.soundhub.R
import com.soundhub.data.api.responses.ReceivedMessageResponse
import com.soundhub.data.dao.UserDao
import com.soundhub.data.datastore.UserCredsStore
import com.soundhub.data.model.Chat
import com.soundhub.data.model.Message
import com.soundhub.data.repository.ChatRepository
import com.soundhub.data.repository.MessageRepository
import com.soundhub.data.websocket.WebSocketClient
import com.soundhub.ui.viewmodels.UiStateDispatcher
import com.soundhub.utils.constants.ApiEndpoints.ChatWebSocket.WS_DELETE_MESSAGE_TOPIC
import com.soundhub.utils.constants.ApiEndpoints.ChatWebSocket.WS_GET_MESSAGES_TOPIC
import com.soundhub.utils.constants.ApiEndpoints.ChatWebSocket.WS_READ_MESSAGE_TOPIC
import com.soundhub.utils.constants.Constants
import com.soundhub.utils.converters.json.LocalDateTimeAdapter
import com.soundhub.utils.converters.json.LocalDateWebSocketAdapter
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.disposables.Disposable
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import ua.naiksoftware.stomp.dto.StompMessage
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.UUID
import javax.inject.Inject

@AndroidEntryPoint
class MessengerAndroidService: Service() {
    private val CHANNEL_ID = "message_channel"

    @Inject
    lateinit var webSocketClient: WebSocketClient
    @Inject
    lateinit var userDao: UserDao
    @Inject
    lateinit var chatRepository: ChatRepository
    @Inject
    lateinit var messageRepository: MessageRepository
    @Inject
    lateinit var uiStateDispatcher: UiStateDispatcher
    @Inject
    lateinit var userCredsStore: UserCredsStore

    private val gson: Gson = GsonBuilder()
        .registerTypeAdapter(LocalDate::class.java, LocalDateWebSocketAdapter())
        .registerTypeAdapter(LocalDateTime::class.java, LocalDateTimeAdapter())
        .create()

    private val coroutineScope = CoroutineScope(Dispatchers.IO + Job())

    override fun onCreate() {
        super.onCreate()
        Log.i("WebSocketService", "Service created")
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        coroutineScope.launch {
            webSocketClient.connect(Constants.SOUNDHUB_WEBSOCKET)
            subscribeToAllChats()
        }
        return START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        webSocketClient.disconnect()
        Log.i("WebSocketService", "Service destroyed")
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    private suspend fun subscribeToAllChats() = coroutineScope.launch {
        userDao.getCurrentUser()?.let { user ->
            val chats: List<Chat> = chatRepository.getAllChatsByUserId(
                userId = user.id
            ).getOrNull().orEmpty()

            with(webSocketClient) {
                val subscriptions: MutableList<Disposable?> = mutableListOf()

                chats.map { it.id }.forEach { id ->
                    val subscription = subscribe(
                        topic = "$WS_GET_MESSAGES_TOPIC/$id",
                        messageListener = ::onReceiveMessageListener,
                        errorListener = ::onSubscribeErrorListener
                    )
                    subscriptions.add(subscription)
                }

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
    }

    private fun onDeleteMessageListener(message: StompMessage) {
        Log.i("MessengerAndroidService", "onDeleteMessageListener[1]: $message")
        coroutineScope.launch {
            runCatching { gson.fromJson(message.payload, UUID::class.java) }
                .onFailure { Log.e("MessengerAndroidService", "onDeleteMessageListener[2]: ${it.stackTraceToString()}") }
                .onSuccess{ uiStateDispatcher.sendDeletedMessage(it) }
        }
    }

    private fun onSubscribeErrorListener(throwable: Throwable) {
        Log.e("MessengerAndroidService", "onSubscribeErrorListener: ${throwable.stackTraceToString()}")
    }

    private fun onReadMessageListener(message: StompMessage) {
        Log.i("MessengerAndroidService", "onReadMessageListener: $message")
        coroutineScope.launch {
            val readMessage: Message = gson.fromJson(message.payload, Message::class.java)
            uiStateDispatcher.sendReadMessage(readMessage)
        }
    }

    private fun onReceiveMessageListener(message: StompMessage) {
        Log.i("MessengerAndroidService", "onReceiveMessageListener: $message")

        val builder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle("Напоминание")
            .setContentText("Пора покормить кота")
            .setAutoCancel(true)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        val notificationManager = NotificationManagerCompat.from(this)
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return
        }
        notificationManager.notify(1, builder.build())

        coroutineScope.launch {
            val receivedMessageResponse: ReceivedMessageResponse = gson
                .fromJson(message.payload, ReceivedMessageResponse::class.java)

            val receivedMessage: Message? = messageRepository.getMessageById(
                messageId = receivedMessageResponse.id
            ).getOrNull()

            receivedMessage?.let {
                uiStateDispatcher.sendReceivedMessage(it)
            }
        }
    }
}