package com.soundhub.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.google.gson.annotations.SerializedName
import com.soundhub.utils.converters.room.UserRoomConverter
import java.io.File
import java.time.LocalDate
import java.util.UUID

@Entity
@TypeConverters(UserRoomConverter::class)
data class User(
    @PrimaryKey
    var id: UUID = UUID.randomUUID(),
    var gender: Gender = Gender.UNKNOWN,
    var avatarUrl: String? = null,
    var avatarImageFile: File? = null,
    var email: String? = "",
    var firstName: String? = "",
    var lastName: String? = "",
    var country: String? = "",

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
    var favoriteArtists: List<Artist> = emptyList()
)