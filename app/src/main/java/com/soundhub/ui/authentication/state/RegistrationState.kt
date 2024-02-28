package com.soundhub.ui.authentication.state

import com.soundhub.data.model.Gender
import com.soundhub.ui.components.forms.IUserDataFormState
import java.time.LocalDate
import java.util.UUID

data class RegistrationState(
    var email: String = "",
    var password: String = "",
    var id: UUID = UUID.randomUUID(),
    override var firstName: String? = "",
    override var lastName: String? = "",
    override var gender: Gender = Gender.Unknown,
    override var country: String? = "",
    override var birthday: LocalDate? = null,
    override var city: String? = "",
    override var description: String? = "",

    // TODO: separate form valid states from this state
    override var isFirstNameValid: Boolean = true,
    override var isLastNameValid: Boolean = true,
    override var isBirthdayValid: Boolean = true,
    var isLoading: Boolean = false,
) : IUserDataFormState
