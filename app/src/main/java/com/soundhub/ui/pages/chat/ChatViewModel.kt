package com.soundhub.ui.pages.chat

import android.util.Log
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.ui.input.pointer.PointerInputScope
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.soundhub.Route
import com.soundhub.data.api.requests.SendMessageRequest
import com.soundhub.data.api.responses.HttpResult
import com.soundhub.data.dao.UserDao
import com.soundhub.data.datastore.UserCredsStore
import com.soundhub.data.enums.ApiStatus
import com.soundhub.data.model.Chat
import com.soundhub.data.model.Message
import com.soundhub.data.model.User
import com.soundhub.data.repository.ChatRepository
import com.soundhub.data.repository.MessageRepository
import com.soundhub.data.states.ChatUiState
import com.soundhub.data.websocket.WebSocketClient
import com.soundhub.ui.events.UiEvent
import com.soundhub.ui.viewmodels.UiStateDispatcher
import com.soundhub.utils.constants.Constants.SOUNDHUB_WEBSOCKET
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(
	private val chatRepository: ChatRepository,
	private val messageRepository: MessageRepository,
	private val uiStateDispatcher: UiStateDispatcher,
	private val userCredsStore: UserCredsStore,
	private val userDao: UserDao,
) : ViewModel() {
	private lateinit var webSocketClient: WebSocketClient
	private val _chatUiState: MutableStateFlow<ChatUiState> = MutableStateFlow(ChatUiState())
	val chatUiState: StateFlow<ChatUiState> = _chatUiState.asStateFlow()

	private val _lazyListState = MutableStateFlow<LazyListState?>(null)
	val lazyListState: StateFlow<LazyListState?> = _lazyListState.asStateFlow()

	init {
		initializeWebSocket()
		viewModelScope.launch {
			launch { onReceiveMessageListener() }
		}
	}

	override fun onCleared() {
		super.onCleared()
		Log.d("ChatViewModel", "ViewModel was cleared")
	}

	private fun initializeWebSocket() = viewModelScope.launch {
		webSocketClient = WebSocketClient(userCredsStore)
		webSocketClient.apply {
			connect(SOUNDHUB_WEBSOCKET)
		}
	}

	private suspend fun onReceiveMessageListener() {
		uiStateDispatcher.receivedMessages.collect { receivedMessage ->
			_chatUiState.update { state ->
				val updatedMessages = state.chat?.messages.orEmpty().toMutableList()
				if (updatedMessages.none { it.id == receivedMessage.id }) {
					updatedMessages.add(receivedMessage)
				}

				val updatedChat = state.chat?.copy()?.apply {
					messages = updatedMessages
				}
				state.copy(chat = updatedChat)
			}
		}
	}

	suspend fun scrollToLastMessage(
		totalMessageCount: Int = lazyListState.value
			?.layoutInfo
			?.totalItemsCount
			?: 0
	) {
		val lazyList = lazyListState.value
		if (totalMessageCount > 0 && lazyList?.isScrollInProgress == false) {
			lazyList.scrollToItem(totalMessageCount - 1)
		}
	}

	suspend fun getMessageById(messageId: UUID): Message? {
		return messageRepository
			.getMessageById(messageId)
			.onSuccessReturn()
	}

	fun activateReplyMessageMode() {
		if (_chatUiState.value.checkedMessages.size == 1) {
			_chatUiState.update { it.copy(
				isReplyMessageModeEnabled = true,
				isCheckMessageModeEnabled = false
			) }
		}
	}

	fun loadChatById(chatId: UUID) = viewModelScope.launch(Dispatchers.IO) {
		getChatById(chatId).firstOrNull()
			?.onSuccessWithContext { response ->
				val currentUser: User? = userDao.getCurrentUser()
				val chat: Chat? = response.body
				val messages: List<Message> = chat?.messages.orEmpty()
				val unreadMessageCount: Int =
					messages.count { it.sender?.id != currentUser?.id && !it.isRead }
				val interlocutor: User? = chat?.participants?.find { it.id != currentUser?.id }

				_chatUiState.update {
					it.copy(
						chat = response.body,
						unreadMessageCount = unreadMessageCount,
						interlocutor = interlocutor,
						status = ApiStatus.SUCCESS
					)
				}
			}
			?.onFailureWithContext { error ->
				val errorEvent: UiEvent = UiEvent.Error(error.errorBody, error.throwable)
				uiStateDispatcher.sendUiEvent(errorEvent)
				_chatUiState.update { it.copy(status = ApiStatus.ERROR) }
			}
	}

	private fun getChatById(id: UUID): Flow<HttpResult<Chat?>> = flow {
		emit(chatRepository.getChatById(id))
	}

	fun deleteChat(chatId: UUID) = viewModelScope.launch(Dispatchers.IO) {
		chatRepository.deleteChatById(chatId)
			.onSuccessWithContext {
				uiStateDispatcher.sendUiEvent(UiEvent.Navigate(Route.Messenger))
			}
			.onFailureWithContext { error ->
				val errorEvent: UiEvent = UiEvent.Error(error.errorBody, error.throwable)
				uiStateDispatcher.sendUiEvent(errorEvent)
			}
	}

	fun setMessageContent(message: String) =
		_chatUiState.update { it.copy(messageContent = message) }

	private fun clearMessageContent() = _chatUiState.update {
		it.copy(messageContent = "")
	}

	fun onSendMessageClick(lazyListState: LazyListState) = viewModelScope.launch(Dispatchers.Main) {
		val (chat) = _chatUiState.value
		val messageCount = chat?.messages?.size ?: 0

		sendMessage()
		if (messageCount > 0)
			 lazyListState.scrollToItem(messageCount - 1)
	}

	suspend fun scrollToMessageById(messageId: UUID?) = withContext(Dispatchers.Main) {
		val messageIndex = _chatUiState.value.chat?.messages
			.orEmpty()
			.indexOfFirst { it.id == messageId }

		val scrollOffset = 10

		if (messageIndex >= 0) {
			lazyListState.value?.scrollToItem(messageIndex + scrollOffset)
		}
	}

	private fun sendMessage() = viewModelScope.launch(Dispatchers.IO) {
		val authorizedUser: User? = userDao.getCurrentUser()
		val (chat, messageContent) = _chatUiState.value
		val replyMode = _chatUiState.value.isReplyMessageModeEnabled

		if (messageContent.isEmpty() || messageContent.isBlank())
			return@launch

		val replyToMessageId: UUID? = if (replyMode)
			_chatUiState.value
				.checkedMessages
				.firstOrNull()?.id
		else null

		val sendMessageRequest = SendMessageRequest(
			chatId = chat?.id,
			userId = authorizedUser?.id,
			content = messageContent,
			replyToMessageId = replyToMessageId
		)

		webSocketClient.sendMessage(
			messageRequest = sendMessageRequest,
			onComplete = {
				Log.i("ChatViewModel", "Message sent successfully")
				clearMessageContent()
				unsetReplyMessageMode()

				viewModelScope.launch {
					scrollToLastMessage()
				}
			},
			onError = { error ->
				Log.e("ChatViewModel", "sendMessage[error]: ${error.stackTraceToString()}")
			}
		)
	}

	fun readVisibleMessagesFromIndex(startIndex: Int) = viewModelScope.launch(Dispatchers.IO) {
		val messages: List<Message> = chatUiState.map { it.chat?.messages }
			.firstOrNull()
			.orEmpty()

		if (startIndex >= messages.size)
			return@launch

		userDao.getCurrentUser()?.let { user ->
			val visibleInterlocutorMessages: List<Message> =
				messages.subList(startIndex, messages.size)
					.filter { it.sender?.id != user.id && !it.isRead }

			visibleInterlocutorMessages.forEach { msg -> readMessage(msg) }
			Log.d("ChatScreen", "visible message: $visibleInterlocutorMessages")
		}
	}

	private fun readMessage(message: Message) = webSocketClient.readMessage(message.id)

	fun deleteMessage(message: Message, deleterId: UUID) = webSocketClient
		.deleteMessage(
			messageId = message.id,
			deleterId = deleterId,
			onComplete = {
				_chatUiState.update {
					val chat: Chat? = it.chat?.copy()
					chat?.messages = chat?.messages.orEmpty().filter { msg -> msg.id != message.id }
					it.copy(chat = chat)
				}
			}
		)

	fun setCheckMessageMode(check: Boolean) = _chatUiState.update {
		it.copy(isCheckMessageModeEnabled = check)
	}

	fun unsetCheckMessagesMode() = _chatUiState.update {
		it.copy(isCheckMessageModeEnabled = false, checkedMessages = emptyList())
	}

	fun unsetReplyMessageMode() = _chatUiState.update {
		it.copy(isReplyMessageModeEnabled = false, checkedMessages = emptyList())
	}

	fun setLazyListState(state: LazyListState) = _lazyListState.update { state }

	private fun uncheckMessage(message: Message) {
		if (_chatUiState.value.isCheckMessageModeEnabled) {
			_chatUiState.update {
				val messages: List<Message> = it.checkedMessages.filter { msg -> msg != message }
				val isCheckMessagesMode = messages.isNotEmpty()
				it.copy(checkedMessages = messages, isCheckMessageModeEnabled = isCheckMessagesMode)
			}
		}
	}

	private fun addCheckedMessage(message: Message) = _chatUiState.update {
		it.copy(checkedMessages = it.checkedMessages + message)
	}

	suspend fun onMessagePointerInputEvent(
		scope: PointerInputScope,
		message: Message,
		isCheckMessagesMode: Boolean,
		checkedMessages: List<Message>
	) = scope.detectTapGestures(
		onLongPress = {
			setCheckMessageMode(true)
			addCheckedMessage(message)
		},
		onTap = {
			if (isCheckMessagesMode) {
				if (message in checkedMessages)
					uncheckMessage(message)
				else addCheckedMessage(message)
			}
		}
	)
}
