package com.soundhub.data.states

import com.soundhub.data.enums.ApiStatus
import com.soundhub.data.states.interfaces.UiStatusState
import com.soundhub.domain.model.Chat

data class MessengerUiState(
	val chats: List<Chat> = emptyList(),
	val unreadMessagesCount: Int = 0,
	override val status: ApiStatus = ApiStatus.LOADING
) : UiStatusState
