package com.soundhub.data.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable
import java.util.UUID

data class Track(
	@SerializedName("id")
	val id: UUID = UUID.randomUUID(),
	val title: String = "",
	val duration: Int
) : Serializable