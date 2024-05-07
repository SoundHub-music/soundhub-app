package com.soundhub.ui.states

import com.soundhub.data.model.Gender
import com.soundhub.ui.components.forms.IUserDataFormState
import java.io.File
import java.time.LocalDate
import java.util.UUID

data class UserFormState(
    override var id: UUID = UUID.randomUUID(),
    override var firstName: String? = "",
    override var lastName: String? = "",
    override var gender: Gender = Gender.UNKNOWN,
    override var country: String? = null,
    override var birthday: LocalDate? = null,
    override var city: String? = null,
    override var description: String? = "",
    override var avatarUrl: String? = null,
    override var languages: List<String> = emptyList(),
    var email: String? = "",
    var avatarImageFile: File? = null,

    override var isFirstNameValid: Boolean = true,
    override var isLastNameValid: Boolean = true,
    override var isBirthdayValid: Boolean = true
): IUserDataFormState
