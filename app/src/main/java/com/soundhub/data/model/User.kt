package com.soundhub.data.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable
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
        if (birthday != other.birthday) return false
        if (city != other.city) return false
        if (description != other.description) return false
        if (languages != other.languages) return false
        if (friends != other.friends) return false
        if (favoriteGenres != other.favoriteGenres) return false
        if (favoriteArtists != other.favoriteArtists) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + gender.hashCode()
        result = 31 * result + (avatarUrl?.hashCode() ?: 0)
        result = 31 * result + (email?.hashCode() ?: 0)
        result = 31 * result + (firstName?.hashCode() ?: 0)
        result = 31 * result + (lastName?.hashCode() ?: 0)
        result = 31 * result + (country?.hashCode() ?: 0)
        result = 31 * result + (birthday?.hashCode() ?: 0)
        result = 31 * result + (city?.hashCode() ?: 0)
        result = 31 * result + (description?.hashCode() ?: 0)
        result = 31 * result + languages.hashCode()
        result = 31 * result + friends.hashCode()
        result = 31 * result + favoriteGenres.hashCode()
        result = 31 * result + favoriteArtists.hashCode()
        return result
    }

    override fun toString(): String {
        return "User(id=$id, gender=$gender, avatarUrl=$avatarUrl, email=$email, firstName=$firstName, lastName=$lastName, country=$country, birthday=$birthday, city=$city, description=$description, languages=$languages, friends=$friends, favoriteGenres=$favoriteGenres, favoriteArtists=$favoriteArtists)"
    }
}