package com.soundhub.data.model

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.google.gson.annotations.SerializedName
import com.soundhub.utils.converters.room.ArtistRoomConverter
import com.soundhub.utils.converters.room.FileRoomConverter
import com.soundhub.utils.converters.room.GenreRoomConverter
import com.soundhub.utils.converters.room.IntListRoomConverter
import com.soundhub.utils.converters.room.LocalDateRoomConverter
import com.soundhub.utils.converters.room.LocalDateTimeRoomConverter
import com.soundhub.utils.converters.room.StringListRoomConverter
import com.soundhub.utils.converters.room.StringMutableListRoomConverter
import com.soundhub.utils.converters.room.UserListRoomConverter
import java.io.Serializable
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.UUID

@Entity
@TypeConverters(
    StringListRoomConverter::class,
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
    var gender: Gender = Gender.UNKNOWN,
    var avatarUrl: String? = null,
    var email: String = "",
    var firstName: String = "",
    var lastName: String = "",
    var country: String? = "",

    @SerializedName("isOnline")
    var isOnline: Boolean = false,

    @SerializedName("lastOnline")
    var lastOnline: LocalDateTime? = null,

    @SerializedName("birthday")
    var birthday: LocalDate? = null,
    var city: String? = "",
    var description: String? = "",
    var languages: MutableList<String> = mutableListOf(),

    @SerializedName("friends")
    var friends: List<User> = emptyList(),

    @SerializedName("favoriteGenres")
    var favoriteGenres: List<Genre> = emptyList(),

    @SerializedName("favoriteArtists")
    var favoriteArtists: List<Artist> = emptyList(),

    @Ignore
    @SerializedName("favoriteArtistsIds")
    var favoriteArtistsIds: List<Int> = emptyList()
): Serializable