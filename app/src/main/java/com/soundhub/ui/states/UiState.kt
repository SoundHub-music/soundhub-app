package com.soundhub.ui.states

import com.soundhub.data.model.Message
import com.soundhub.data.model.User
import com.soundhub.data.websocket.WebSocketClient

data class UiState(
    val isSearchBarActive: Boolean = false,
    val searchBarText: String = "",

    val galleryImageUrls: List<String> = emptyList(),
    val currentRoute: String? = null,
    val authorizedUser: User? = null,

    val isCheckMessagesMode: Boolean = false,
    val checkedMessages: List<Message> = emptyList(),
    val webSocketClient: WebSocketClient? = null
)
