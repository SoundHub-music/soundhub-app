package com.soundhub.data.model

import com.google.gson.annotations.SerializedName
import java.time.LocalDate
import java.util.UUID

data class User(
    @SerializedName("id")
    var id: UUID = UUID.randomUUID(),
    var gender: Gender = Gender.UNKNOWN,
    var avatarUrl: String? = null,
    var email: String? = "",
    var firstName: String? = "",
    var lastName: String? = "",
    var country: String? = "",

    @SerializedName("birthday")
    var birthday: LocalDate? = null,
    var city: String? = "",
    var description: String? = "",
    var languages: MutableList<String> = mutableListOf(),

    @SerializedName("friends")
    var friends: MutableList<User> = mutableListOf(),

    @SerializedName("favoriteGenres")
    var favoriteGenres: MutableList<Genre> = mutableListOf(),

    @SerializedName("favoriteArtists")
    var favoriteArtists: MutableList<Artist> = mutableListOf()
)