package com.soundhub.domain.states

import com.soundhub.domain.enums.Gender
import com.soundhub.domain.states.interfaces.IUserDataFormState
import java.time.LocalDate
import java.util.UUID

data class UserFormState(
	override var id: UUID = UUID.randomUUID(),
	override var firstName: String = "",
	override var lastName: String = "",
	override var gender: Gender = Gender.UNKNOWN,
	override var country: String? = null,
	override var birthday: LocalDate? = null,
	override var city: String? = null,
	override var description: String? = "",
	override var avatarUrl: String? = null,
	override var languages: MutableList<String> = mutableListOf(),
	var email: String = "",

	override var isFirstNameValid: Boolean = true,
	override var isLastNameValid: Boolean = true,
	override var isBirthdayValid: Boolean = true
) : IUserDataFormState
