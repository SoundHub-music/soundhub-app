package com.soundhub.data.states

import com.soundhub.data.enums.ApiStatus
import com.soundhub.data.model.Chat

data class MessengerUiState(
	val chats: List<Chat> = emptyList(),
	val status: ApiStatus = ApiStatus.LOADING,
	val unreadMessagesCount: Int = 0
)
