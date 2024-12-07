package com.soundhub.domain.model

import com.google.gson.annotations.SerializedName
import java.time.LocalDateTime
import java.util.UUID

data class Message(
	@SerializedName("chat_id")
	val chatId: UUID,
	val replyToMessageId: UUID? = null,
	var isRead: Boolean = false,

	override val id: UUID,
	override var author: User?,
	override val createdAt: LocalDateTime,
	override var content: String
) : ContentEntity() {
	fun checkIfSentByUser(user: User?) = author?.id == user?.id
	fun isNotReadAndNotSentBy(user: User?) = !checkIfSentByUser(user) && !isRead
}
