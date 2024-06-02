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
import com.soundhub.utils.converters.room.UserListRoomConverter
import java.io.Serializable
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.UUID

@Entity
@TypeConverters(
    StringListRoomConverter::class,
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
    var languages: List<String> = emptyList(),

    @SerializedName("friends")
    var friends: List<User> = emptyList(),

    @SerializedName("favoriteGenres")
    var favoriteGenres: List<Genre> = emptyList(),

    @SerializedName("favoriteArtists")
    var favoriteArtists: List<Artist> = emptyList(),

    @Ignore
    @SerializedName("favoriteArtistsIds")
    var favoriteArtistsIds: List<Int> = emptyList()
): Serializable {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as User

        if (id != other.id) return false
        if (gender != other.gender) return false
        if (avatarUrl != other.avatarUrl) return false
        if (email != other.email) return false
        if (firstName != other.firstName) return false
        if (lastName != other.lastName) return false
        if (country != other.country) return false
        if (isOnline != other.isOnline) return false
        if (lastOnline != other.lastOnline) return false
        if (birthday != other.birthday) return false
        if (city != other.city) return false
        if (description != other.description) return false
        if (languages != other.languages) return false
        if (friends != other.friends) return false
        if (favoriteGenres != other.favoriteGenres) return false
        if (favoriteArtists != other.favoriteArtists) return false
        if (favoriteArtistsIds != other.favoriteArtistsIds) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + gender.hashCode()
        result = 31 * result + (avatarUrl?.hashCode() ?: 0)
        result = 31 * result + email.hashCode()
        result = 31 * result + firstName.hashCode()
        result = 31 * result + lastName.hashCode()
        result = 31 * result + (country?.hashCode() ?: 0)
        result = 31 * result + isOnline.hashCode()
        result = 31 * result + (lastOnline?.hashCode() ?: 0)
        result = 31 * result + (birthday?.hashCode() ?: 0)
        result = 31 * result + (city?.hashCode() ?: 0)
        result = 31 * result + (description?.hashCode() ?: 0)
        result = 31 * result + languages.hashCode()
        result = 31 * result + friends.hashCode()
        result = 31 * result + favoriteGenres.hashCode()
        result = 31 * result + favoriteArtists.hashCode()
        result = 31 * result + favoriteArtistsIds.hashCode()
        return result
    }
}