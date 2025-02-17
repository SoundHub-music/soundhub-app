package com.soundhub.domain.states.interfaces

import com.soundhub.domain.enums.Gender
import com.soundhub.domain.model.interfaces.IUser
import java.time.LocalDate
import java.util.UUID

interface IUserDataFormState : IUser {
	var id: UUID
	override var firstName: String
	override var lastName: String
	override var gender: Gender
	override var country: String?
	override var birthday: LocalDate?
	override var city: String?
	override var description: String?
	override var avatarUrl: String?
	override var languages: MutableList<String>

	var isFirstNameValid: Boolean
	var isLastNameValid: Boolean
	var isBirthdayValid: Boolean
}