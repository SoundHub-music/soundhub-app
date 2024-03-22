package com.soundhub.data.model

import com.google.gson.annotations.SerializedName
import java.time.LocalDateTime
import java.util.UUID

data class Message(
    @SerializedName("id")
    val id: UUID = UUID.randomUUID(),

    @SerializedName("sender")
    val sender: User? = null,

    @SerializedName("timestamp")
    val timestamp: LocalDateTime = LocalDateTime.now(),
    val content: String = "",
    val isRead: Boolean = false,
)
