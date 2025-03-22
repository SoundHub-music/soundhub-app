package com.soundhub.domain.states

import com.soundhub.data.enums.ApiStatus
import com.soundhub.domain.model.Chat
import com.soundhub.domain.states.interfaces.UiStatusState

data class MessengerUiState(
	val chats: List<Chat> = emptyList(),
	val unreadMessagesCount: Int = 0,
	override val status: ApiStatus = ApiStatus.NOT_LAUNCHED
) : UiStatusState
