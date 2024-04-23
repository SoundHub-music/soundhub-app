package com.soundhub.data.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable
import java.time.LocalDateTime
import java.util.UUID

data class Message(
    @SerializedName("id")
    val id: UUID = UUID.randomUUID(),

    @SerializedName("sender")
    val sender: User?,

    @SerializedName("timestamp")
    val timestamp: LocalDateTime = LocalDateTime.now(),
    val content: String = "",
    val isRead: Boolean = false,

    @SerializedName("chat")
    val chat: Chat?
): Serializable {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Message
        return id == other.id &&
                sender == other.sender &&
                timestamp == other.timestamp &&
                content == other.content &&
                isRead == other.isRead
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + (sender?.hashCode() ?: 0)
        result = 31 * result + timestamp.hashCode()
        result = 31 * result + content.hashCode()
        result = 31 * result + isRead.hashCode()
        return result
    }

    override fun toString(): String {
        return "Message(id=$id, sender=$sender, timestamp=$timestamp, content='$content', isRead=$isRead)"
    }
}
