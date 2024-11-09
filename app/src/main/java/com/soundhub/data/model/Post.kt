package com.soundhub.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.soundhub.utils.converters.room.LocalDateTimeRoomConverter
import com.soundhub.utils.converters.room.StringListRoomConverter
import com.soundhub.utils.converters.room.UserRoomConverter
import com.soundhub.utils.converters.room.UserSetRoomConverter
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
	override val id: UUID,
	override var author: User?,
	override var createdAt: LocalDateTime = LocalDateTime.now(),
	override var content: String = "",

	var images: List<String> = emptyList(),
	var likes: Set<User> = emptySet()
) : ContentEntity()
