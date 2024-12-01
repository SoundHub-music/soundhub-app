package com.soundhub.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.google.gson.annotations.SerializedName
import com.soundhub.utils.converters.room.GenreRoomConverter
import com.soundhub.utils.converters.room.LocalDateRoomConverter
import java.io.Serializable
import java.time.LocalDate
import java.util.UUID

@Entity
@TypeConverters(
	LocalDateRoomConverter::class,
	GenreRoomConverter::class
)
data class Album(
	@PrimaryKey
	var id: UUID = UUID.randomUUID(),
	var title: String = "",

	@SerializedName("releaseDate")
	var releaseDate: LocalDate? = null,

	@SerializedName("genre")
	var genre: Genre? = null
) : Serializable