package com.soundhub.ui.components.forms

import com.soundhub.data.model.Gender
import java.time.LocalDate

interface IUserDataFormState {
    var firstName: String?
    var lastName: String?
    var gender: Gender
    var country: String?
    var birthday: LocalDate?
    var city: String?
    var description: String?
    var avatarURL: String?
    var languages: List<String>

    var isFirstNameValid: Boolean
    var isLastNameValid: Boolean
    var isBirthdayValid: Boolean
}