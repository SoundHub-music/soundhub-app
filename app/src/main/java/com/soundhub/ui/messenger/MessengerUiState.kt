package com.soundhub.ui.messenger

import com.soundhub.data.enums.ApiStatus
import com.soundhub.data.model.Chat

data class MessengerUiState(
    val chats: List<Chat> = emptyList(),
    val status: ApiStatus = ApiStatus.LOADING,
    val unreadMessagesTotal: Int = 0
)
