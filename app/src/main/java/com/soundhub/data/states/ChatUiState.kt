package com.soundhub.data.states

import com.soundhub.data.enums.ApiStatus
import com.soundhub.data.model.Chat
import com.soundhub.data.model.Message
import com.soundhub.data.model.User

data class ChatUiState(
	var chat: Chat? = null,
	val messageContent: String = "",
	val unreadMessageCount: Int = 0,
	val status: ApiStatus = ApiStatus.LOADING,
	val interlocutor: User? = null,
	val checkedMessages: List<Message> = emptyList(),

	val isCheckMessageModeEnabled: Boolean = false,
	val isReplyMessageModeEnabled: Boolean = false
)
