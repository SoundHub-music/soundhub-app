package com.soundhub.data.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable
import java.util.UUID

data class Chat(
    @SerializedName("id")
    val id: UUID = UUID.randomUUID(),

    @SerializedName("messages")
    var messages: List<Message> = emptyList(),

    @SerializedName("participants")
    val participants: List<User> = emptyList(),
    val isGroup: Boolean = false,
    val chatName: String? = null,

    @SerializedName("createdBy")
    val createdBy: User
): Serializable {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Chat

        if (id != other.id) return false
        if (messages != other.messages) return false
        if (participants != other.participants) return false
        if (isGroup != other.isGroup) return false
        if (chatName != other.chatName) return false
        if (createdBy != other.createdBy) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + messages.hashCode()
        result = 31 * result + participants.hashCode()
        result = 31 * result + isGroup.hashCode()
        result = 31 * result + (chatName?.hashCode() ?: 0)
        result = 31 * result + (createdBy.hashCode())
        return result
    }

    override fun toString(): String {
        return "Chat(id=$id, messages=$messages, participants=$participants, isGroup=$isGroup, chatName=$chatName, createdBy=$createdBy)"
    }

}