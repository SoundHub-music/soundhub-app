package com.soundhub.data.states.interfaces

import com.soundhub.data.enums.Gender
import com.soundhub.data.model.interfaces.IUser
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