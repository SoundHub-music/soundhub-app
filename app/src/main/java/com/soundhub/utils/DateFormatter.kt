package com.soundhub.utils

import java.time.LocalDateTime

class DateFormatter {
    companion object {
        fun toFullStringDate(dateTime: LocalDateTime): String {
            val now = LocalDateTime.now()
            if (now.dayOfYear == dateTime.dayOfYear && now.year == dateTime.year)
                return "Сегодня в ${dateTime.hour}:${dateTime.minute}"

            return "${dateTime.dayOfYear} ${dateTime.month.value}"
        }
    }
}