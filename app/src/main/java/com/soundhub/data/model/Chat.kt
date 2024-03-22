package com.soundhub.data.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable
import java.util.UUID

data class Chat(
    @SerializedName("id")
    val id: UUID = UUID.randomUUID(),

    val lastMessage: String = "",
    val unreadMessageCount: Int = 0,

    @SerializedName("messages")
    val messages: List<Message> = emptyList(),

    @SerializedName("participants")
    val participants: List<User?> = emptyList()
): Serializable