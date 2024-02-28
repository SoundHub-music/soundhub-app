package com.soundhub.utils

import java.time.LocalDate
import java.time.LocalDateTime
import java.time.Month

class DateFormatter {
    companion object {
        private val russianLocaleMonths: Map<Month, String> = mapOf(
            Month.JANUARY to "января",
            Month.FEBRUARY to "февраля",
            Month.MARCH to "марта",
            Month.APRIL to "апреля",
            Month.MAY to "мая",
            Month.JUNE to "июня",
            Month.JULY to "июля",
            Month.AUGUST to "августа",
            Month.SEPTEMBER to "сентября",
            Month.OCTOBER to "октября",
            Month.NOVEMBER to "ноября",
            Month.DECEMBER to "декабря"
        )

        fun toFullStringDate(dateTime: LocalDateTime): String {
            val now = LocalDateTime.now()
            if (now.dayOfYear == dateTime.dayOfYear && now.year == dateTime.year)
                return "Сегодня в ${dateTime.hour}:${dateTime.minute}"

            return "${dateTime.dayOfYear} ${dateTime.month.value}"
        }

        fun getRelativeDate(dateTime: LocalDateTime): String {
            val now = LocalDateTime.now()
            return when {
                now.dayOfYear == dateTime.dayOfYear && now.year == dateTime.year -> {
                    val timeDifference: Int
                    if (now.hour > dateTime.hour) {
                        timeDifference = now.hour - dateTime.hour
                        return "$timeDifference ${getCorrectStringHour(timeDifference)} назад"
                    }
                    timeDifference = now.minute - dateTime.minute
                    if (timeDifference == 0) return "Сейчас"
                    return "$timeDifference ${getCorrectStringMinute(timeDifference)} назад"
                }
                now.isEqual(dateTime) -> "Сейчас"
                else -> {
                    val time: String = "${dateTime.hour}:${dateTime.minute}"
                    "${dateTime.dayOfMonth} ${russianLocaleMonths[dateTime.month]} в $time"
                }
            }
        }

        fun getStringMonthAndDay(date: LocalDate): String {
            return "${date.dayOfMonth} ${russianLocaleMonths[date.month]}"
        }


        private fun getCorrectStringMinute(minutes: Int): String {
            return when {
                minutes % 10 in 2..4 -> "минуты"
                minutes % 10 == 1 && minutes != 11 -> "минута"
                else -> "минут"
            }
        }

        private fun getCorrectStringHour(hours: Int): String {
            return when {
                hours % 10 == 1 && hours != 11 -> "час"
                hours % 10 in 2..4 -> "часа"
                else -> "часов"
            }
        }
    }
}