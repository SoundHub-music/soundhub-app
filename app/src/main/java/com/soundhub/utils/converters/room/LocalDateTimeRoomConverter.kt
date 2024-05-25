package com.soundhub.utils.converters.room

import androidx.room.TypeConverter
import com.soundhub.utils.constants.Constants
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class LocalDateTimeRoomConverter {
    private val formatter: DateTimeFormatter
        get() = DateTimeFormatter.ofPattern(Constants.DATETIME_FORMAT)

    @TypeConverter
    fun fromLocalDateTime(date: LocalDateTime?): String {
        return date.toString()
    }

    @TypeConverter
    fun toLocalDateTime(stringDate: String): LocalDateTime? =
        if (stringDate == "null") null
        else LocalDateTime.parse(stringDate, formatter)
}