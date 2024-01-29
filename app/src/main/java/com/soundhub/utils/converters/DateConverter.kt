package com.soundhub.utils.converters

import androidx.room.TypeConverter
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class DateConverter {
    @TypeConverter
    fun fromLocalDate(date: LocalDate?): String {
        return date.toString()
    }

    @TypeConverter
    fun toLocalDate(stringDate: String): LocalDate? {
        return LocalDate.parse(stringDate, DateTimeFormatter.ofPattern(stringDate))
    }
}