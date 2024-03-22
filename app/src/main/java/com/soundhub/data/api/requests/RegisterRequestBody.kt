package com.soundhub.data.api.requests

import com.soundhub.data.model.Artist
import com.soundhub.data.model.Gender
import com.soundhub.data.model.Genre
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
)
