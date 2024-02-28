package com.soundhub.ui.edit_profile.state

import com.soundhub.data.model.Gender
import com.soundhub.data.model.User
import com.soundhub.ui.components.forms.IUserDataFormState
import java.time.LocalDate

data class EditDataFormState(
    override var firstName: String? = "",
    override var lastName: String? = "",
    override var gender: Gender = Gender.Unknown,
    override var country: String? = "",
    override var birthday: LocalDate? = null,
    override var city: String? = "",
    override var description: String? = "",
    override var isFirstNameValid: Boolean = true,
    override var isLastNameValid: Boolean = true,
    override var isBirthdayValid: Boolean = true

): IUserDataFormState {
    constructor(user: User?): this() {
        if (user != null) {
            firstName = user.firstName
            lastName = user.lastName
            gender = user.gender
            country = user.country
            birthday = user.birthday
            city = user.city
            description = user.description
        }
    }
}