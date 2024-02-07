package com.soundhub.ui.authentication.state

import com.soundhub.data.model.Artist
import com.soundhub.data.model.Gender
import com.soundhub.data.model.Genre
import com.soundhub.ui.components.forms.UserDataForm
import java.time.LocalDate
import java.util.UUID

data class RegistrationState(
    var email: String = "",
    var password: String = "",
    var id: UUID = UUID.randomUUID(),
    override var firstName: String = "",
    override var lastName: String = "",
    override var gender: Gender = Gender.Unknown,
    override var country: String = "",
    override var birthday: LocalDate? = null,
    override var city: String = "",
    override var languages: List<String> = emptyList(),
    override var description: String = "",
    var token: String? = null,

    override var isFirstNameValid: Boolean = true,
    override var isLastNameValid: Boolean = true,
    override var isBirthdayValid: Boolean = true,
    var isLoading: Boolean = false,

    var chosenGenres: MutableList<Genre> = emptyList<Genre>().toMutableList(),
    var chosenArtists: MutableList<Artist> = emptyList<Artist>().toMutableList()
) : UserDataForm
