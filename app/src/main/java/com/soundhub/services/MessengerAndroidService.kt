package com.soundhub.services

import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.app.RemoteInput
import com.google.gson.Gson
import com.soundhub.R
import com.soundhub.data.api.requests.SendMessageRequest
import com.soundhub.data.api.responses.internal.ReceivedMessageResponse
import com.soundhub.data.api.websocket.WebSocketClient
import com.soundhub.data.datastore.UserCredsStore
import com.soundhub.data.local_database.dao.UserDao
import com.soundhub.domain.model.Chat
import com.soundhub.domain.model.Message
import com.soundhub.domain.model.User
import com.soundhub.domain.repository.ChatRepository
import com.soundhub.domain.repository.MessageRepository
import com.soundhub.ui.activities.MainActivity
import com.soundhub.ui.viewmodels.UiStateDispatcher
import com.soundhub.utils.NotificationHelper
import com.soundhub.utils.constants.ApiEndpoints.ChatWebSocket.WS_DELETE_MESSAGE_TOPIC
import com.soundhub.utils.constants.ApiEndpoints.ChatWebSocket.WS_GET_MESSAGES_TOPIC
import com.soundhub.utils.constants.ApiEndpoints.ChatWebSocket.WS_READ_MESSAGE_TOPIC
import com.soundhub.utils.constants.Constants
import com.soundhub.utils.extensions.intent.getSerializableExtraExtended
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import ua.naiksoftware.stomp.dto.StompMessage
import java.io.Serializable
import java.util.UUID
import javax.inject.Inject
import com.soundhub.Route.Messenger.Chat as ChatRoute

@AndroidEntryPoint
class MessengerAndroidService : Service() {
	companion object {
		private val LOG_CLASSNAME = MessengerAndroidService::class.simpleName

		private const val CHANNEL_ID = "com.soundhub.messenger"
		private const val NOTIFICATION_GROUP_ID = "com.soundhub.services.MessengerAndroidService"

		const val BROADCAST_MESSAGE_KEY = "message"
		const val MESSAGE_RECEIVER = "message.receiver.get"
		const val READ_MESSAGE_RECEIVER = "message.receiver.read"
		const val DELETE_MESSAGE_RECEIVER = "message.receiver.delete"

		const val READ_MESSAGE_ACTION = "action.read_message"
		const val MESSAGE_ID_KEY = "message.id"

		const val REPLY_ACTION = "action.reply"
		const val REPLY_INPUT_KEY = "reply_input"
		const val CHAT_ID_KEY = "chat.id"
		const val NOTIFICATION_ID = "notification.id"

		const val REPLY_MESSAGE_REQUEST_CODE = 100
		const val READ_MESSAGE_REQUEST_CODE = 101
		const val FOLLOW_CHAT_REQUEST_CODE = 102

		const val MESSAGE_REPLAY_COUNT = 32
	}

	inner class LocalBinder : Binder() {
		fun getService(): MessengerAndroidService = context
	}

	private val receivedMessagesCache = MutableSharedFlow<Message>(
		replay = MESSAGE_REPLAY_COUNT,
		onBufferOverflow = BufferOverflow.DROP_OLDEST
	)

	@Inject
	lateinit var webSocketClient: WebSocketClient

	@Inject
	lateinit var userDao: UserDao

	@Inject
	lateinit var chatRepository: ChatRepository

	@Inject
	lateinit var messageRepository: MessageRepository

	@Inject
	lateinit var userCredsStore: UserCredsStore

	@Inject
	lateinit var gson: Gson

	@Inject
	lateinit var uiStateDispatcher: UiStateDispatcher

	private lateinit var notificationHelper: NotificationHelper

	private val binder = LocalBinder()

	private val context = this

	private val coroutineScope = CoroutineScope(Dispatchers.IO + Job())

	override fun onCreate() {
		super.onCreate()
		Log.i(LOG_CLASSNAME, "Service created")
	}

	override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
		Log.i(LOG_CLASSNAME, "Service started with flag: $flags, startId: $startId")
		startWithAction(intent)

		coroutineScope.launch {
			notificationHelper = NotificationHelper(
				context = context,
				userCredsStore = userCredsStore
			)

			webSocketClient.connect(Constants.SOUNDHUB_WEBSOCKET)
			subscribeToAllChats()
		}

		return START_STICKY
	}


	override fun onDestroy() {
		super.onDestroy()
		webSocketClient.disconnect()
		Log.i(LOG_CLASSNAME, "Service destroyed")
	}

	override fun onUnbind(intent: Intent?): Boolean {
		webSocketClient.disconnect()
		return super.onUnbind(intent)
	}

	override fun onBind(intent: Intent): IBinder = binder

	private fun startWithAction(intent: Intent?) {
		intent?.let {
			Log.d(LOG_CLASSNAME, "startWithAction[1]: received action ${intent.action}")
			when (intent.action) {
				REPLY_ACTION -> processReplyMessage(intent)
				READ_MESSAGE_ACTION -> processReadMessage(intent)
				else -> return
			}
		}
	}

	private fun processReadMessage(intent: Intent) {
		val notificationId = intent.getIntExtra(NOTIFICATION_ID, 0)
		intent.getSerializableExtraExtended(MESSAGE_ID_KEY, UUID::class.java)?.let {
			Log.d(LOG_CLASSNAME, "processReadMessage[1]: request for reading message with id $it")
			readMessage(
				messageId = it,
				onReadMessage = {
					Log.i(
						LOG_CLASSNAME,
						"readMessage[1]: message with id $it marked as read successfully"
					)
					notificationHelper.cancelNotification(notificationId)
				},
				onErrorReadMessage = { error ->
					Log.e(LOG_CLASSNAME, "readMessage[2]: ${error.stackTraceToString()}")
				}
			)
		}

	}

	private fun processReplyMessage(intent: Intent) {
		val replyBundle: Bundle? = RemoteInput.getResultsFromIntent(intent)

		val message = replyBundle?.getCharSequence(REPLY_INPUT_KEY)?.toString()
		val chatId: UUID? = intent.getSerializableExtraExtended(CHAT_ID_KEY, UUID::class.java)
		val replyToMessageId = intent.getSerializableExtraExtended(MESSAGE_ID_KEY, UUID::class.java)
		val notificationId = intent.getIntExtra(NOTIFICATION_ID, 0)

		Log.d(LOG_CLASSNAME, "processReplyMessage[1] -> Chat id: $chatId")
		Log.d(LOG_CLASSNAME, "processReplyMessage[2] -> Notification id: $notificationId")
		Log.d(LOG_CLASSNAME, "processReplyMessage[3] -> message: $message")
		Log.d(LOG_CLASSNAME, "processReplyMessage[4] -> replyToMessageId: $replyToMessageId")

		if (chatId != null && message != null)
			sendMessage(
				chatId = chatId,
				message = message,
				replyToMessageId = replyToMessageId,
				onSendMessage = {
					Log.i("$LOG_CLASSNAME", "processReplyMessage[4]: Message was sent successfully")
					notificationHelper.cancelNotification(notificationId)
				},
				onErrorSendMessage = { error ->
					Log.e(LOG_CLASSNAME, "processReplyMessage[5]: ${error.stackTraceToString()}")
				}
			)
	}

	private fun sendMessage(
		chatId: UUID,
		message: String,
		replyToMessageId: UUID? = null,
		onSendMessage: () -> Unit = {},
		onErrorSendMessage: (Throwable) -> Unit = {}
	) = coroutineScope.launch {
		val authorizedUser: User? = userDao.getCurrentUser()
		val messageRequestBody = SendMessageRequest(
			chatId = chatId,
			userId = authorizedUser?.id,
			content = message,
			replyToMessageId = replyToMessageId
		)

		Log.d(LOG_CLASSNAME, "sendWebSocketMessage[1]: $messageRequestBody")
		Log.d(LOG_CLASSNAME, "sendWebSocketMessage[2] -> replyToMessageId: $replyToMessageId")

		webSocketClient.sendMessage(
			messageRequest = messageRequestBody,
			onComplete = onSendMessage,
			onError = onErrorSendMessage
		)
	}

	private fun readMessage(
		messageId: UUID,
		onReadMessage: () -> Unit = {},
		onErrorReadMessage: (Throwable) -> Unit = {}
	) = coroutineScope.launch {
		webSocketClient.readMessage(
			messageId,
			onComplete = onReadMessage,
			onError = onErrorReadMessage
		)
	}

	private fun subscribeToAllChats() = coroutineScope.launch {
		userDao.getCurrentUser()?.let { user ->
			val chats: List<Chat> = chatRepository.getAllChatsByUserId(user.id)
				.getOrNull()
				.orEmpty()

			with(webSocketClient) {
				chats.map { it.id }.forEach { id ->
					subscribe(
						topic = "$WS_GET_MESSAGES_TOPIC/$id",
						messageListener = ::onReceiveMessageListener,
						errorListener = ::onSubscribeErrorListener
					)
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
		Log.d(LOG_CLASSNAME, "onDeleteMessageListener[1]: $message")
		coroutineScope.launch {
			runCatching { gson.fromJson(message.payload, UUID::class.java) }
				.onFailure {
					Log.e(
						LOG_CLASSNAME,
						"onDeleteMessageListener[2]: ${it.stackTraceToString()}"
					)
				}
				.onSuccess { messageId ->
					sendMessageBroadcast(DELETE_MESSAGE_RECEIVER, messageId)
				}
		}
	}

	private fun onSubscribeErrorListener(throwable: Throwable) {
		Log.e(
			LOG_CLASSNAME,
			"onSubscribeErrorListener[1]: ${throwable.stackTraceToString()}"
		)
	}

	private fun onReadMessageListener(message: StompMessage) = coroutineScope.launch {
		Log.d(LOG_CLASSNAME, "onReadMessageListener[1]: $message")
		coroutineScope.launch {
			val readMessage: Message = gson.fromJson(message.payload, Message::class.java)
			sendMessageBroadcast(READ_MESSAGE_RECEIVER, readMessage)
		}
	}

	private fun onReceiveMessageListener(stompMessage: StompMessage) = coroutineScope.launch {
		Log.d(LOG_CLASSNAME, "onReceiveMessageListener[1]: $stompMessage")
		val response: ReceivedMessageResponse = gson.fromJson(
			stompMessage.payload,
			ReceivedMessageResponse::class.java
		)

		val authorizedUser: User? = userDao.getCurrentUser()
		val receivedMessage: Message? = messageRepository
			.getMessageById(response.id)
			.getOrNull()

		receivedMessage?.let { message ->
			receivedMessagesCache.emit(message)
			val currentAppRoute: String? = uiStateDispatcher.uiState
				.firstOrNull()
				?.currentRoute

			val isNotSender = authorizedUser?.id != message.author?.id
			val currentChatRoute =
				ChatRoute.getStringRouteWithNavArg(receivedMessage.chatId.toString())
			val isNotChatRoute = currentAppRoute != currentChatRoute

			if (isNotSender && isNotChatRoute)
				processNotification(message)

			sendMessageBroadcast(MESSAGE_RECEIVER, message)
		}
	}

	private fun processNotification(message: Message) {
		with(notificationHelper) {
			val notificationId: Int = generateNotificationId(message)
			val notificationBuilder = buildNotification(message, notificationId)

			loadAvatar(message, notificationBuilder)
			createNotificationChannelIfNotExists(
				channelId = CHANNEL_ID,
				channelName = context.getString(R.string.notification_channel_messenger_name),
				importance = NotificationManagerCompat.IMPORTANCE_HIGH,
				setExtraParameters = { channel ->
					with(channel) {
						enableLights(true)
						enableVibration(true)
					}
				}
			)
			createNotificationGroupIfNotExists(NOTIFICATION_GROUP_ID)
			sendNotification(notificationBuilder, notificationId)
		}
	}

	private fun sendMessageBroadcast(intentAction: String, obj: Serializable?) {
		Intent(intentAction).apply {
			putExtra(BROADCAST_MESSAGE_KEY, obj)
			sendBroadcast(this)
		}
	}

	private fun buildNotification(
		message: Message,
		notificationId: Int
	): NotificationCompat.Builder {
		Log.d(LOG_CLASSNAME, "buildNotification[1] -> notificationId: $notificationId")
		val intent = Intent(context, MainActivity::class.java).apply {
			flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
			putExtra(Constants.CHAT_NAV_ARG, message.chatId.toString())
		}

		val pendingIntent = PendingIntent.getActivity(
			context,
			FOLLOW_CHAT_REQUEST_CODE,
			intent,
			PendingIntent.FLAG_IMMUTABLE
		)

		return NotificationCompat.Builder(this, CHANNEL_ID)
			.setSmallIcon(R.drawable.ic_launcher_foreground)
			.setGroup(NOTIFICATION_GROUP_ID)
			.setContentTitle(message.author?.getFullName())
			.setContentText(message.content)
			.setAutoCancel(true)
			.setShowWhen(true)
			.setContentIntent(pendingIntent)
			.setPriority(NotificationCompat.PRIORITY_HIGH)
			.apply {
				createNotificationActions(message, notificationId).forEach {
					addAction(it)
				}
			}
	}

	private fun createNotificationActions(
		message: Message,
		notificationId: Int
	): List<NotificationCompat.Action> {
		val replyInput = RemoteInput.Builder(REPLY_INPUT_KEY)
			.setLabel(this.getString(R.string.notification_messenger_action_reply_label))
			.build()

		val replyPendingIntent = notificationHelper.createPendingIntent(
			action = REPLY_ACTION,
			requestCode = REPLY_MESSAGE_REQUEST_CODE,
			flags = PendingIntent.FLAG_MUTABLE or PendingIntent.FLAG_UPDATE_CURRENT,
			extras = Bundle().apply {
				putSerializable(CHAT_ID_KEY, message.chatId)
				putSerializable(MESSAGE_ID_KEY, message.id)
				putInt(NOTIFICATION_ID, notificationId)
			}
		)

		val readMessagePendingIntent = notificationHelper.createPendingIntent(
			action = READ_MESSAGE_ACTION,
			requestCode = READ_MESSAGE_REQUEST_CODE,
			flags = PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT,
			extras = Bundle().apply {
				putSerializable(MESSAGE_ID_KEY, message.id)
				putInt(NOTIFICATION_ID, notificationId)
			}
		)

		val readMessageAction = NotificationCompat.Action.Builder(
			null,
			context.getString(R.string.notification_messenger_action_read_message_title),
			readMessagePendingIntent
		).build()

		val replyAction = NotificationCompat.Action.Builder(
			null,
			context.getString(R.string.notification_messenger_action_reply_title),
			replyPendingIntent
		)
			.addRemoteInput(replyInput)
			.build()

		return listOf(replyAction, readMessageAction)
	}
}