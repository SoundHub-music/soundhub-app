package com.soundhub.domain.model

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.soundhub.data.model.interfaces.IUser
import com.soundhub.domain.enums.Gender
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
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.UUID

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
}