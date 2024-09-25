package com.soundhub.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.soundhub.utils.converters.room.LocalDateTimeRoomConverter
import com.soundhub.utils.converters.room.StringListRoomConverter
import com.soundhub.utils.converters.room.UserRoomConverter
import com.soundhub.utils.converters.room.UserSetRoomConverter
import java.io.Serializable
import java.time.LocalDateTime
import java.util.UUID

@Entity
@TypeConverters(
	StringListRoomConverter::class,
	LocalDateTimeRoomConverter::class,
	UserSetRoomConverter::class,
	UserRoomConverter::class
)
data class Post(
	@PrimaryKey
	val id: UUID = UUID.randomUUID(),
	var author: User?,
	var publishDate: LocalDateTime = LocalDateTime.now(),
	val content: String = "",
	var images: List<String> = emptyList(),
	var likes: Set<User> = emptySet()
) : Serializable
