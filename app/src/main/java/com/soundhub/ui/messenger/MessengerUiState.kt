package com.soundhub.ui.messenger

import com.soundhub.data.enums.ApiStatus
import com.soundhub.data.model.Chat
import com.soundhub.data.model.User

data class MessengerUiState(
    val authorizedUser: User? = null,
    val chats: List<Chat> = emptyList(),
    val status: ApiStatus = ApiStatus.LOADING,
    val unreadMessagesTotal: Int = 0
)
