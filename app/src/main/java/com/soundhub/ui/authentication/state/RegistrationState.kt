package com.soundhub.ui.authentication.state

import com.soundhub.data.model.IUser
import java.time.LocalDate
import java.util.UUID

data class RegistrationState(
    override var email: String = "",
    var password: String? = null,
    override var id: UUID = UUID.randomUUID(),
    override var firstName: String = "",
    override var lastName: String = "",
    override var country: String = "",
    override var birthday: LocalDate? = null,
    override var city: String = "",
    override var languages: List<String> = emptyList(),
    override var description: String = "",
    override var token: String? = null,

    var isFirstNameValid: Boolean = true,
    var isLastNameValid: Boolean = true,
    var isBirthdayValid: Boolean = true
) : IUser
