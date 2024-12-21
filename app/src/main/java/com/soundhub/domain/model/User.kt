package com.soundhub.domain.model

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.soundhub.R
import com.soundhub.data.model.interfaces.IUser
import com.soundhub.domain.enums.Gender
import com.soundhub.utils.constants.Constants
import com.soundhub.utils.converters.room.ArtistRoomConverter
import com.soundhub.utils.converters.room.FileRoomConverter
import com.soundhub.utils.converters.room.GenreRoomConverter
import com.soundhub.utils.converters.room.IntListRoomConverter
import com.soundhub.utils.converters.room.LocalDateRoomConverter
import com.soundhub.utils.converters.room.LocalDateTimeRoomConverter
import com.soundhub.utils.converters.room.StringListRoomConverter
import com.soundhub.utils.converters.room.StringMutableListRoomConverter
import com.soundhub.utils.converters.room.UUIDRoomConverter
import com.soundhub.utils.converters.room.UserListRoomConverter
import com.soundhub.utils.lib.DateFormatter
import com.soundhub.utils.lib.UiText
import java.time.Duration
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.UUID
import kotlin.math.abs

@Entity
@TypeConverters(
	StringListRoomConverter::class,
	UUIDRoomConverter::class,
	StringMutableListRoomConverter::class,
	IntListRoomConverter::class,
	LocalDateRoomConverter::class,
	LocalDateTimeRoomConverter::class,
	UserListRoomConverter::class,
	ArtistRoomConverter::class,
	GenreRoomConverter::class,
	FileRoomConverter::class
)
data class User(
	@PrimaryKey
	var id: UUID = UUID.randomUUID(),
	var email: String = "",
	override var gender: Gender = Gender.UNKNOWN,
	override var avatarUrl: String? = null,
	override var firstName: String = "",
	override var lastName: String = "",
	override var country: String? = "",
	override var birthday: LocalDate? = null,
	override var city: String? = "",
	override var description: String? = "",
	override var languages: MutableList<String> = mutableListOf(),

	var online: Boolean = false,
	var lastOnline: LocalDateTime? = null,
	var friends: List<User> = emptyList(),

	var favoriteGenres: List<Genre> = emptyList(),
	var favoriteArtists: List<Artist> = emptyList(),

	@Ignore
	var favoriteArtistsIds: List<Int> = emptyList()
) : IUser {
	fun getFullName(): String = "$firstName $lastName".trim()

	fun getUserLocation(): String? {
		if (city.isNullOrEmpty() && country.isNullOrEmpty()) return null
		if (country?.isNotEmpty() == true && city.isNullOrEmpty()) return country

		return "$country, $city"
	}

	fun updateOnlineStatusIndicator(
		updateCallback: (icon: Int, color: Int, text: UiText) -> Unit
	) {
		val maxOnlineStatusMinutes: Long = Constants.SET_OFFLINE_DELAY_ON_STOP / 1000 / 60

		val minuteDifference: Long = lastOnline?.let {
			abs(Duration.between(LocalDateTime.now(), lastOnline).toMinutes())
		} ?: Long.MAX_VALUE

		if (online || minuteDifference < maxOnlineStatusMinutes)
			return updateCallback(
				R.drawable.online_indicator,
				R.color.online_status,
				UiText.StringResource(R.string.online_indicator_user_online)
			)

		val lastOnlineString: String? = lastOnline?.let { DateFormatter.getRelativeDate(it) }

		val statusText: UiText = if (lastOnlineString?.isNotEmpty() == true) {
			UiText.StringResource(
				R.string.online_indicator_user_offline_with_time,
				lastOnlineString
			)
		} else UiText.StringResource(R.string.online_indicator_user_offline)

		return updateCallback(
			R.drawable.offline_indicator,
			R.color.offline_status,
			statusText
		)
	}
}