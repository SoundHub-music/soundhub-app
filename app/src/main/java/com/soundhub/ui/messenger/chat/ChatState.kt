package com.soundhub.ui.messenger.chat

import com.soundhub.data.model.Message
import com.soundhub.data.model.User

data class ChatState(
    var messages: List<Message> = emptyList(),
    val interlocutor: User? = null
)
