package com.soundhub.ui.shared.forms

import android.net.Uri
import java.time.LocalDate

interface IUserFormViewModel {
	val formHandler: FormHandler

	fun setFirstName(value: String)
	fun setLastName(value: String)
	fun setBirthday(value: LocalDate?)
	fun setGender(value: String)
	fun setCountry(value: String)
	fun setCity(value: String)
	fun setDescription(value: String)
	fun setLanguages(languages: List<String>)
	fun setAvatar(avatarUrl: Uri?)
}