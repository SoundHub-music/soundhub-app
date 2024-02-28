package com.soundhub.data.model

import java.util.UUID

data class Chat(
    val id: UUID = UUID.randomUUID(),
    val lastMessage: String = "",
    val unreadMessageCount: Int = 0,
    val messages: List<Message> = emptyList(),
    val participants: List<User?> = emptyList()
)