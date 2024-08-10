package com.soundhub.utils.converters.room

import android.util.Log
import androidx.room.TypeConverter
import com.soundhub.utils.constants.Constants.FALLBACK_LOCAL_DATETIME_PATTERN
import com.soundhub.utils.constants.Constants.LOCAL_DATETIME_PATTERN
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class LocalDateTimeRoomConverter {
	private val formatter = DateTimeFormatter.ofPattern(LOCAL_DATETIME_PATTERN)
	private val fallbackFormatter = DateTimeFormatter.ofPattern(FALLBACK_LOCAL_DATETIME_PATTERN)

	@TypeConverter
	fun fromLocalDateTime(date: LocalDateTime?): String {
		return date?.format(formatter) ?: "null"
	}

	@TypeConverter
	fun toLocalDateTime(stringDate: String): LocalDateTime? = try {
		if (stringDate == "null") null
		else LocalDateTime.parse(stringDate, formatter)
	} catch (e: Exception) {
		Log.d("LocalDateTimeRoomConverter", "toLocalDateTime[error]: ${e.stackTraceToString()}")
		LocalDateTime.parse(stringDate, fallbackFormatter)
	}
}