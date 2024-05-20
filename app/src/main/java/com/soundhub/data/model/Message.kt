package com.soundhub.data.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable
import java.time.LocalDateTime
import java.util.UUID

data class Message(
    val id: UUID = UUID.randomUUID(),

    @SerializedName("sender")
    val sender: User?,

    @SerializedName("timestamp")
    val timestamp: LocalDateTime = LocalDateTime.now(),
    val content: String = "",
    var isRead: Boolean = false,
): Serializable
