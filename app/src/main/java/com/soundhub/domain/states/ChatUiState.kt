package com.soundhub.domain.states

import com.soundhub.data.api.responses.internal.PageableMessagesResponse
import com.soundhub.data.enums.ApiStatus
import com.soundhub.domain.model.Chat
import com.soundhub.domain.model.Message
import com.soundhub.domain.model.User

data class ChatUiState(
	var chat: Chat? = null,
	val messageContent: String = "",
	val pagedMessages: PageableMessagesResponse? = null,
	val cachedMessages: List<Message> = emptyList(),
	val unreadMessageCount: Int = 0,
	val status: ApiStatus = ApiStatus.NOT_LAUNCHED,
	val interlocutor: User? = null,
	val checkedMessages: Set<Message> = emptySet(),

	val isCheckMessageModeEnabled: Boolean = false,
	val isReplyMessageModeEnabled: Boolean = false
)
