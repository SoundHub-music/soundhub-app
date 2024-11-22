package com.soundhub.data.states

import com.soundhub.data.enums.ApiStatus
import com.soundhub.data.model.Chat
import com.soundhub.data.states.interfaces.UiStatusState

data class MessengerUiState(
	val chats: List<Chat> = emptyList(),
	val unreadMessagesCount: Int = 0,
	override val status: ApiStatus = ApiStatus.LOADING
) : UiStatusState
