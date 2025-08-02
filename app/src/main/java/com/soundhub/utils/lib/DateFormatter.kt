package com.soundhub.utils.lib

import com.soundhub.R
import com.soundhub.SoundHubApp
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

class DateFormatter {
	companion object {
		private val CONTEXT = SoundHubApp.getAppResources()

		fun getRelativeDate(dateTime: LocalDateTime): String {
			val now = LocalDateTime.now()
			val nowString = CONTEXT.getString(R.string.chat_time_now)

			val isDaysEqual = now.dayOfYear == dateTime.dayOfYear
			val isYearsEqual = now.year == dateTime.year

			if (now.isEqual(dateTime)) {
				return nowString
			}

			if (isDaysEqual && isYearsEqual) {
				return getTodayRelativeDate(dateTime)
			}

			return getLocalizedDate(dateTime)
		}

		private fun getLocalizedDate(dateTime: LocalDateTime): String {
			val stringBuilder = StringBuilder()

			val time = stringBuilder
				.append(dateTime.hour)
				.append(":")
				.append(getTwoDigitMinuteString(dateTime.minute))
				.toString()
				.also {
					stringBuilder.clear()
				}

			return stringBuilder
				.append(dateTime.dayOfMonth)
				.append(" ")
				.append(getLocalizedMonth(dateTime))
				.append(" ")
				.append(time)
				.toString()
		}

		private fun getTodayRelativeDate(dateTime: LocalDateTime): String {
			val agoString = CONTEXT.getString(R.string.chat_time_ago)
			val nowString = CONTEXT.getString(R.string.chat_time_now)

			val timeDifference: Int
			val stringBuilder = StringBuilder()
			val now = LocalDateTime.now()

			if (now.hour > dateTime.hour) {
				timeDifference = now.hour - dateTime.hour

				return stringBuilder
					.append(timeDifference)
					.append(" ")
					.append(getLocalizedHour(timeDifference))
					.append(" ")
					.append(agoString)
					.toString()
			}

			timeDifference = now.minute - dateTime.minute

			if (timeDifference == 0) {
				return nowString
			}

			return stringBuilder
				.append(timeDifference)
				.append(" ")
				.append(getLocalizedMinute(timeDifference))
				.append(" ")
				.append(agoString)
				.toString()
		}

		private fun getLocalizedDateFormatter(): DateTimeFormatter {
			val currentLanguage = Locale.getDefault().language
			val locale = Locale(currentLanguage)

			return DateTimeFormatter
				.ofPattern("MMMM", locale)
				.withLocale(locale)
		}

		fun getLocalizedMonth(date: LocalDateTime): String {
			val formatter = getLocalizedDateFormatter()

			return date
				.format(formatter)
				.replaceFirstChar { it.lowercase() }
		}

		fun getLocalizedMonth(date: LocalDate): String {
			val formatter = getLocalizedDateFormatter()

			return date
				.format(formatter)
				.replaceFirstChar { it.lowercase() }
		}

		private fun getTwoDigitMinuteString(minutes: Int): String {
			return if (minutes < 10) "0$minutes"
			else minutes.toString()
		}

		fun getStringDate(date: LocalDate, includeYear: Boolean = false): String {
			val stringBuilder = StringBuilder()
			val stringDate = stringBuilder
				.append(date.dayOfMonth)
				.append(" ")
				.append(getLocalizedMonth(date))

			if (includeYear) {
				stringDate
					.append(" ")
					.append(date.year)
			}

			return stringDate.toString()
		}

		private fun getLocalizedMinute(minutes: Int): String {
			return CONTEXT.getQuantityString(R.plurals.minute, minutes)
		}

		private fun getLocalizedHour(hours: Int): String {
			return CONTEXT.getQuantityString(R.plurals.hour, hours)
		}

		fun getHourAndMinuteWithSeparator(time: LocalDateTime, separator: String = ":"): String {
			val hour = time.hour.toString()
			var minute = time.minute.toString()

			if (time.minute < 10)
				minute = "0$minute"

			return "$hour$separator$minute"
		}
	}
}