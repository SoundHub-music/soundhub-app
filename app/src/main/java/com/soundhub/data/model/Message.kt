package com.soundhub.data.model

import java.time.LocalDateTime
import java.util.UUID

data class Message(
    val id: UUID = UUID.randomUUID(),
    val sender: User? = null,
    val timestamp: LocalDateTime = LocalDateTime.now(),
    val content: String = "",
    val isRead: Boolean = false,
)
