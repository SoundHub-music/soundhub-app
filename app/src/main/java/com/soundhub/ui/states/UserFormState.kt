package com.soundhub.ui.states

import com.soundhub.data.model.Gender
import com.soundhub.data.model.User
import com.soundhub.ui.components.forms.IUserDataFormState
import java.time.LocalDate

data class UserFormState(
    override var firstName: String? = "",
    override var lastName: String? = "",
    override var gender: Gender = Gender.UNKNOWN,
    override var country: String? = null,
    override var birthday: LocalDate? = null,
    override var city: String? = null,
    override var description: String? = "",
    override var avatarURL: String? = null,
    override var languages: List<String> = emptyList(),

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
            city = user.city
            birthday = user.birthday
            description = user.description
            languages = user.languages
            avatarURL = user.avatarUrl
        }
    }
}
