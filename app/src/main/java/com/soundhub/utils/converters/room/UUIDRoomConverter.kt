package com.soundhub.utils.converters.room

import androidx.room.TypeConverter
import java.util.UUID

class UUIDRoomConverter {
	@TypeConverter
	fun toStringUUID(id: UUID): String {
		return id.toString()
	}

	@TypeConverter
	fun fromStringUser(stringId: String): UUID {
		return UUID.fromString(stringId)
	}
}