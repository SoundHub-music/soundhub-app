package com.soundhub.presentation.shared.forms

import android.net.Uri
import java.time.LocalDate

data class FormHandler(
	var onFirstNameChange: (String) -> Unit = {},
	var onLastNameChange: (String) -> Unit = {},
	var onBirthdayChange: (LocalDate?) -> Unit = {},
	var onAvatarChange: (Uri) -> Unit = {},
	var onDescriptionChange: (String) -> Unit = {},
	var onGenderChange: (String) -> Unit = {},
	var onCountryChange: (String) -> Unit = {},
	var onCityChange: (String) -> Unit = {},
	var onLanguagesChange: (List<String>) -> Unit = {}
)
