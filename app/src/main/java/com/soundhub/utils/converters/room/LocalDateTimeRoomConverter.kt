package com.soundhub.utils.converters.room

import androidx.room.TypeConverter
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class LocalDateTimeRoomConverter {
    private val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")

    @TypeConverter
    fun fromLocalDateTime(date: LocalDateTime?): String {
        return date.toString()
    }

    @TypeConverter
    fun toLocalDateTime(stringDate: String): LocalDateTime? =
        if (stringDate == "null") null
        else LocalDateTime.parse(stringDate, formatter)
}