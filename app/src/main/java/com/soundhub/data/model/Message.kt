package com.soundhub.data.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable
import java.time.LocalDateTime
import java.util.UUID

data class Message(
	val id: UUID = UUID.randomUUID(),

	@SerializedName("chat_id")
	val chatId: UUID = UUID.randomUUID(),

	val replyToMessageId: UUID? = null,

	val sender: User?,

	val timestamp: LocalDateTime = LocalDateTime.now(),
	val content: String = "",
	var isRead: Boolean = false,
) : Serializable
