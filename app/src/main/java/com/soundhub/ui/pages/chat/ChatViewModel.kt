package com.soundhub.ui.pages.chat

import android.util.Log
import androidx.compose.animation.core.tween
import androidx.compose.foundation.gestures.animateScrollBy
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.ui.input.pointer.PointerInputScope
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.soundhub.Route
import com.soundhub.data.api.requests.SendMessageRequest
import com.soundhub.data.api.responses.internal.HttpResult
import com.soundhub.data.api.websocket.WebSocketClient
import com.soundhub.data.datastore.UserCredsStore
import com.soundhub.data.enums.ApiStatus
import com.soundhub.data.local_database.dao.UserDao
import com.soundhub.data.model.Chat
import com.soundhub.data.model.Message
import com.soundhub.data.model.User
import com.soundhub.data.sources.MessageSource
import com.soundhub.data.states.ChatUiState
import com.soundhub.domain.repository.ChatRepository
import com.soundhub.domain.repository.MessageRepository
import com.soundhub.ui.events.UiEvent
import com.soundhub.ui.viewmodels.UiStateDispatcher
import com.soundhub.utils.constants.Constants
import com.soundhub.utils.constants.Constants.SOUNDHUB_WEBSOCKET
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flatMapLatest
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

	private val lazyListState = MutableStateFlow<LazyListState?>(null)
	private val _messageSource = MutableStateFlow<MessageSource?>(null)
	private var pagingDataState: Flow<PagingData<Message>>

	init {
		initializeWebSocket()
		pagingDataState = getMessagePage()
	}

	fun getPagedMessages() = pagingDataState

	private fun updateMessageSource(chatId: UUID): MessageSource {
		val messageSourceInstance = MessageSource(
			messageRepository = messageRepository,
			chatId = chatId
		)

		_messageSource.update { messageSourceInstance }
		return messageSourceInstance
	}

	@OptIn(ExperimentalCoroutinesApi::class)
	fun getMessagePage(): Flow<PagingData<Message>> {
		return _chatUiState
			.map { it.chat }
			.filterNotNull()
			.distinctUntilChanged()
			.flatMapLatest { chat ->
				Pager(
					config = PagingConfig(pageSize = Constants.DEFAULT_MESSAGE_PAGE_SIZE),
					initialKey = Constants.DEFAULT_MESSAGE_PAGE,
					pagingSourceFactory = { updateMessageSource(chatId = chat.id) }
				).flow.cachedIn(viewModelScope)
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

	suspend fun scrollToLastMessage(reverse: Boolean = false) {
		val lazyList = lazyListState.value
		val totalItemCount = lazyList?.layoutInfo?.totalItemsCount ?: 0

		if (totalItemCount > 0 && lazyList?.isScrollInProgress == false) {
			lazyList.scrollToItem(if (reverse) 0 else totalItemCount)
		}
	}

	suspend fun animateScrollToLastMessage(reverse: Boolean = false) {
		val slowScrollAnimationSpec = tween<Float>(durationMillis = 500)
		val lazyList = lazyListState.value
		val layoutInfo = lazyList?.layoutInfo
		val totalItemCount = layoutInfo?.totalItemsCount ?: 0
		val viewportOffset = layoutInfo?.viewportEndOffset ?: 0
		val firstVisibleItemScrollOffset = lazyList?.firstVisibleItemScrollOffset ?: 0

		layoutInfo?.let {
			var totalOffset = totalItemCount * viewportOffset - firstVisibleItemScrollOffset

			if (reverse)
				totalOffset = -totalOffset

			lazyList.animateScrollBy(totalOffset.toFloat(), slowScrollAnimationSpec)
		}

	}

	suspend fun getMessageById(messageId: UUID): Message? {
		return messageRepository
			.getMessageById(messageId)
			.onSuccessReturn()
	}

	fun cacheMessages(list: List<Message>) = _chatUiState.update { it.copy(cachedMessages = list) }

	fun activateReplyMessageMode() {
		if (_chatUiState.value.checkedMessages.size == 1) {
			_chatUiState.update {
				it.copy(
					isReplyMessageModeEnabled = true,
					isCheckMessageModeEnabled = false
				)
			}
		}
	}

	fun loadChatById(chatId: UUID) = viewModelScope.launch(Dispatchers.IO) {
		getChatById(chatId).firstOrNull()
			?.onSuccessWithContext { response ->
				val currentUser: User? = userDao.getCurrentUser()
				val chat: Chat? = response.body
				val interlocutor: User? = chat?.participants?.find { it.id != currentUser?.id }

				Log.d("ChatViewModel", _chatUiState.value.pagedMessages.toString())

				_chatUiState.update {
					it.copy(
						chat = response.body,
						interlocutor = interlocutor,
						status = ApiStatus.SUCCESS
					)
				}
			}?.onFailureWithContext { error ->
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

	fun onSendMessageClick() = viewModelScope.launch(Dispatchers.Main) {
		sendMessage()
		scrollToLastMessage(reverse = true)
	}

	suspend fun scrollToMessageById(messageId: UUID?) = withContext(Dispatchers.Main) {
		val messages = _chatUiState.value.cachedMessages
		val lazyListState = lazyListState.value
		val messageIndex = messages.indexOfFirst { it.id == messageId }

		if (messageIndex >= 0) {
			lazyListState?.scrollToItem(messageIndex)
		}
	}

	private fun sendMessage() = viewModelScope.launch(Dispatchers.IO) {
		val (chat: Chat?, messageContent: String) = _chatUiState.value
		val authorizedUser = userDao.getCurrentUser()

		if (messageContent.isBlank() || messageContent.isEmpty())
			return@launch

		val sendMessageRequest: SendMessageRequest = createSendMessageRequest(
			chatId = chat?.id,
			content = messageContent,
			sender = authorizedUser
		)

		webSocketClient.sendMessage(
			messageRequest = sendMessageRequest,
			onComplete = ::onMessageSentSuccessfully,
			onError = ::onMessageSendError
		)
	}

	private fun createSendMessageRequest(
		chatId: UUID?,
		content: String,
		sender: User?
	): SendMessageRequest {
		val replyToMessageId = getReplyToMessageId()

		return SendMessageRequest(
			chatId = chatId,
			userId = sender?.id,
			content = content,
			replyToMessageId = replyToMessageId
		)
	}

	private fun onMessageSentSuccessfully() {
		Log.i("ChatViewModel", "Message sent successfully")
		clearMessageContent()
		unsetReplyMessageMode()
	}

	private fun onMessageSendError(error: Throwable) {
		Log.e("ChatViewModel", "sendMessage[error]: ${error.stackTraceToString()}")
	}

	private fun getReplyToMessageId(): UUID? {
		val isEnabled = _chatUiState.value.isReplyMessageModeEnabled
		val checkedMessages = _chatUiState.value.checkedMessages

		return if (isEnabled)
			checkedMessages.firstOrNull()?.id
		else null
	}

	suspend fun readVisibleMessagesFromIndex(startIndex: Int, messages: List<Message>) {
		if (startIndex >= messages.size || messages.isEmpty())
			return

		userDao.getCurrentUser()?.let { user ->
			messages.subList(startIndex, messages.size)
				.filter { !it.checkIfSentByUser(user) && !it.isRead }
				.forEach { msg -> readMessage(msg) }
		}
	}

	private fun readMessage(message: Message) = webSocketClient.readMessage(message.id)

	fun deleteMessage(message: Message, deleterId: UUID) = webSocketClient
		.deleteMessage(
			messageId = message.id,
			deleterId = deleterId,
		)

	fun setCheckMessageMode(check: Boolean) = _chatUiState.update {
		it.copy(isCheckMessageModeEnabled = check)
	}

	fun unsetCheckMessagesMode() = _chatUiState.update {
		it.copy(
			isCheckMessageModeEnabled = false,
			checkedMessages = emptyList()
		)
	}

	fun unsetReplyMessageMode() {
		val isReplyModeEnabled = _chatUiState.value.isReplyMessageModeEnabled
		if (isReplyModeEnabled) {
			_chatUiState.update {
				it.copy(isReplyMessageModeEnabled = false, checkedMessages = emptyList())
			}
		}
	}

	fun setLazyListState(state: LazyListState) = lazyListState.update { state }

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
