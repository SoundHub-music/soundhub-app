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
): Serializable {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Chat
        return id == other.id &&
                lastMessage == other.lastMessage &&
                unreadMessageCount == other.unreadMessageCount &&
                messages == other.messages &&
                participants == other.participants
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + lastMessage.hashCode()
        result = 31 * result + unreadMessageCount
        result = 31 * result + messages.hashCode()
        result = 31 * result + participants.hashCode()
        return result
    }

    override fun toString(): String {
        return "Chat(id=$id, lastMessage='$lastMessage', unreadMessageCount=$unreadMessageCount, messages=$messages, participants=$participants)"
    }
}