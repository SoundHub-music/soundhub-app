package com.soundhub.data.model.interfaces

import com.soundhub.domain.enums.Gender
import java.io.Serializable
import java.time.LocalDate

// an interface that describes user's personal information
interface IUser : Serializable {
	var gender: Gender
	var avatarUrl: String?
	var firstName: String
	var lastName: String
	var country: String?
	var birthday: LocalDate?
	var city: String?
	var description: String?
	var languages: MutableList<String>
}