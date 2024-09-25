package com.soundhub.data.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable
import java.util.UUID

data class Chat(
	@SerializedName("id")
	val id: UUID = UUID.randomUUID(),

	@SerializedName("participants")
	val participants: List<User> = emptyList(),
	val isGroup: Boolean = false,
	val chatName: String? = null,

	@SerializedName("createdBy")
	val createdBy: User
) : Serializable