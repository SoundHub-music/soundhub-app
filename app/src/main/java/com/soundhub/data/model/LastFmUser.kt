package com.soundhub.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity
data class LastFmUser(
	@PrimaryKey
	val name: String,

	@SerializedName("key")
	val sessionKey: String,
	val subscriber: Int
)
