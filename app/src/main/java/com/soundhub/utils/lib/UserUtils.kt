package com.soundhub.utils.lib

import android.content.Context
import com.soundhub.R
import com.soundhub.data.model.User
import java.time.Duration
import java.time.LocalDateTime
import kotlin.math.abs

class UserUtils {
	companion object {
		/**
		 * updates online status icon, color and text and sends it to the callback
		 * @param context Android app context
		 * @param user user instance for whom the online status needs to be updated
		 * @param updateCallback callback via which the updated status is transmitted
		 */
		fun updateOnlineStatusIndicator(
			context: Context,
			user: User?,
			updateCallback: (icon: Int, color: Int, text: String) -> Unit
		) {
			val isOnline: Boolean = user?.isOnline ?: false
			val maxOnlineStatusMinutes = 5
			val minuteDifference: Long = user?.lastOnline?.let {
				abs(Duration.between(LocalDateTime.now(), user.lastOnline).toMinutes())
			} ?: Long.MAX_VALUE

			val (indicatorIcon, indicatorColor, indicatorText) = if (
				isOnline || minuteDifference < maxOnlineStatusMinutes
			) {
				Triple(
					R.drawable.online_indicator,
					R.color.online_status,
					context.getString(R.string.online_indicator_user_online)
				)
			} else {
				val lastOnlineString =
					user?.lastOnline?.let { DateFormatter.getRelativeDate(it) } ?: ""
				val statusText = if (lastOnlineString.isNotEmpty()) {
					context.getString(
						R.string.online_indicator_user_offline_with_time,
						lastOnlineString
					)
				} else context.getString(R.string.online_indicator_user_offline)

				Triple(
					R.drawable.offline_indicator,
					R.color.offline_status,
					statusText
				)
			}

			updateCallback(indicatorIcon, indicatorColor, indicatorText)
		}

		/**
		 * returns string of country and city (if exists) separated by comma.
		 * If country and city don't exist returns empty string
		 * If only country exists returns country
		 * @param city user city
		 * @param country user country
		 * @return string with country and city separated by comma
		 */
		fun getUserLocation(city: String?, country: String?): String {
			return if (city.isNullOrEmpty() && country.isNullOrEmpty()) ""
			else if (country?.isNotEmpty() == true && city.isNullOrEmpty()) country
			else "$country, $city"
		}
	}
}