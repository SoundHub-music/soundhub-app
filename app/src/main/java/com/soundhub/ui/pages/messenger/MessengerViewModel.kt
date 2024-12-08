package com.soundhub.ui.pages.messenger

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.soundhub.Route
import com.soundhub.data.api.responses.internal.UnreadMessagesResponse
import com.soundhub.data.datastore.UserCredsStore
import com.soundhub.data.datastore.model.UserPreferences
import com.soundhub.data.enums.ApiStatus
import com.soundhub.data.local_database.dao.UserDao
import com.soundhub.data.states.MessengerUiState
import com.soundhub.domain.events.UiEvent
import com.soundhub.domain.model.Chat
import com.soundhub.domain.model.Message
import com.soundhub.domain.model.User
import com.soundhub.domain.receivers.MessageReceiver
import com.soundhub.domain.repository.ChatRepository
import com.soundhub.domain.repository.MessageRepository
import com.soundhub.domain.services.MessengerAndroidService
import com.soundhub.domain.services.MessengerAndroidService.Companion.BROADCAST_MESSAGE_KEY
import com.soundhub.ui.viewmodels.UiStateDispatcher
import com.soundhub.utils.lib.SearchUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class MessengerViewModel @Inject constructor(
	private val chatRepository: ChatRepository,
	private val uiStateDispatcher: UiStateDispatcher,
	private val messageRepository: MessageRepository,
	private val userDao: UserDao,
	userCredsStore: UserCredsStore,
) : ViewModel() {
	private val uiState = uiStateDispatcher.uiState
	val userCreds: Flow<UserPreferences> = userCredsStore.getCreds()

	private val MAX_MESSAGE_PREVIEW_LENGTH = 20

	private val _messengerUiState = MutableStateFlow(MessengerUiState())
	val messengerUiState = _messengerUiState.asStateFlow()

	private var messageReceiver: MessageReceiver<Message> = MessageReceiver(
		intentName = BROADCAST_MESSAGE_KEY,
		clazz = Message::class.java,
		messageHandler = { message ->
			uiStateDispatcher.sendReceivedMessage(message)
		}
	)

	private var readMessageReceiver: MessageReceiver<Message> = MessageReceiver(
		intentName = BROADCAST_MESSAGE_KEY,
		clazz = Message::class.java,
		messageHandler = { message ->
			uiStateDispatcher.sendReadMessage(message)
		}
	)

	private var deletedMessageReceiver: MessageReceiver<UUID> = MessageReceiver(
		intentName = BROADCAST_MESSAGE_KEY,
		clazz = UUID::class.java,
		messageHandler = { messageId ->
			uiStateDispatcher.sendDeletedMessage(messageId)
		}
	)

	override fun onCleared() {
		super.onCleared()
		Log.d("MessengerViewModel", "viewmodel was cleared")
		_messengerUiState.update { MessengerUiState() }
	}

	fun getMessageReceiversWithIntentFilters() = listOf(
		messageReceiver to MessengerAndroidService.MESSAGE_RECEIVER,
		readMessageReceiver to MessengerAndroidService.READ_MESSAGE_RECEIVER,
		deletedMessageReceiver to MessengerAndroidService.DELETE_MESSAGE_RECEIVER
	)

	fun getMessageReceivers() = listOf(
		messageReceiver,
		readMessageReceiver,
		deletedMessageReceiver
	)

	suspend fun getLastMessageByChatId(chatId: UUID): Message? {
		return messageRepository.getPagedMessages(
			chatId = chatId,
			pageSize = 10,
			page = 1
		).onSuccessReturn()
			?.content
			?.firstOrNull()
	}

	fun updateUnreadMessageCount() = viewModelScope.launch(Dispatchers.IO) {
		messageRepository.getAllUnreadMessages()
			.onSuccessWithContext { response ->
				response.body?.let { body ->
					_messengerUiState.update {
						it.copy(unreadMessagesCount = body.count)
					}

				}
			}
	}

	suspend fun getUnreadChatCount(): Int {
		val authorizedUser: User? = userDao.getCurrentUser()
		val messages = messageRepository.getAllUnreadMessages()
			.onSuccessReturn()
			?.messages
			.orEmpty()

		return messages
			.filter { msg -> msg.author?.id != authorizedUser?.id }
			.groupBy { msg -> msg.chatId }
			.count()
	}

	fun onChatCardClick(chat: Chat) = viewModelScope.launch(Dispatchers.Main) {
		val route = Route.Messenger.Chat.getRouteWithNavArg(chat.id.toString())
		uiStateDispatcher.sendUiEvent(UiEvent.Navigate(route))
	}

	suspend fun getUnreadMessageCountByChatId(chatId: UUID?): Int {
		val unreadMessages: UnreadMessagesResponse? = messageRepository.getAllUnreadMessages()
			.onSuccessReturn()

		return unreadMessages?.messages
			?.filter { msg -> msg.chatId == chatId }
			?.size ?: 0
	}

	suspend fun filterChats(
		chats: List<Chat>,
		searchBarText: String,
		authorizedUser: User?
	): List<Chat> {
		if (searchBarText.isEmpty())
			return chats

		delay(500)
		return chats.filter { chat ->
			val otherUser = chat.participants.firstOrNull { it.id != authorizedUser?.id }
			otherUser != null && SearchUtils.compareWithUsername(otherUser, searchBarText)
		}
	}

	suspend fun prepareMessagePreview(prefix: String, lastMessage: Message): String {
		val authorizedUser: User? = uiState.map { it.authorizedUser }.firstOrNull()
		var lastMessageContent = lastMessage.content

		if (lastMessageContent.length > MAX_MESSAGE_PREVIEW_LENGTH) {
			lastMessageContent = "${lastMessageContent.substring(0, MAX_MESSAGE_PREVIEW_LENGTH)}..."
		}

		lastMessageContent = if (lastMessage.author?.id == authorizedUser?.id) {
			"$prefix $lastMessageContent"
		} else "${lastMessage.author?.firstName}: $lastMessageContent".trim()

		return lastMessageContent
	}

	fun loadChats() = viewModelScope.launch(Dispatchers.IO) {
		val chats = getChats()
		_messengerUiState.update {
			it.copy(chats = chats)
		}
	}

	private suspend fun getChats(): List<Chat> {
		var chats: MutableList<Chat> = mutableListOf()
		userDao.getCurrentUser()?.let { user ->
			chatRepository.getAllChatsByUserId(user.id)
				.onSuccess { response ->
					val responseChats = response.body.orEmpty().filter { it.totalMessages > 0 }
					_messengerUiState.update { it.copy(status = ApiStatus.SUCCESS) }
					chats.addAll(responseChats)
				}
				.onFailure { error ->
					val errorEvent: UiEvent = UiEvent.Error(error.errorBody, error.throwable)
					uiStateDispatcher.sendUiEvent(errorEvent)
					_messengerUiState.update {
						it.copy(status = ApiStatus.ERROR)
					}
				}
		}

		return chats
	}
}
