package com.soundhub.data.api.requests

import com.soundhub.data.model.Artist
import com.soundhub.data.model.Gender
import com.soundhub.data.model.Genre
import com.soundhub.ui.states.RegistrationState
import java.time.LocalDate

data class RegisterRequestBody(
    var email: String = "",
    var password: String = "",
    var firstName: String = "",
    var lastName: String = "",
    var birthday: LocalDate? = null,
    var city: String? = null,
    var country: String? = null,
    var gender: Gender = Gender.UNKNOWN,
    var avatarUrl: String? = null,
    var description: String? = "",
    var languages: List<String> = emptyList(),
    var favoriteGenres: List<Genre> = emptyList(),
    var favoriteArtists: List<Artist> = emptyList()
) {
    constructor(registerState: RegistrationState) : this() {
        this.email = registerState.email
        this.password = registerState.password
        this.firstName = registerState.firstName ?: ""
        this.lastName = registerState.lastName ?: ""
        this.birthday = registerState.birthday
        this.city = registerState.city
        this.country = registerState.country
        this.gender = registerState.gender
        this.avatarUrl = registerState.avatarURL
        this.description = registerState.description
        this.languages = registerState.languages
        this.favoriteGenres = registerState.favoriteGenres
        this.favoriteArtists = registerState.favoriteArtists
    }

}

