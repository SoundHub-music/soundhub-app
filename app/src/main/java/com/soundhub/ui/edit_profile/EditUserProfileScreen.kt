package com.soundhub.ui.edit_profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.soundhub.data.model.Gender
import com.soundhub.ui.components.forms.UserDataForm
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import java.lang.IllegalArgumentException
import java.time.LocalDate

data class EditData(
    override var firstName: String = "",
    override var lastName: String = "",
    override var gender: Gender = Gender.Unknown,
    override var country: String = "",
    override var birthday: LocalDate? = null,
    override var city: String = "",
    override var languages: List<String> = emptyList(),
    override var description: String = "",
    override var isFirstNameValid: Boolean = true,
    override var isLastNameValid: Boolean = true,
    override var isBirthdayValid: Boolean = true

): UserDataForm

@Composable
fun EditUserProfileScreen() {
    val formState = MutableStateFlow(EditData())

    fun onFirstNameChange(value: String) = formState.update {
        it.copy(firstName = value)
    }

    fun onLastNameChange(value: String) = formState.update {
        it.copy(lastName = value)
    }

    fun onBirthdateChange(value: LocalDate?) = formState.update {
        it.copy(birthday = value)
    }

    fun onDescriptionChange(value: String) = formState.update {
        it.copy(description = value)
    }

    fun onGenderChange(value: String) = formState.update {
        try {
            it.copy(gender = Gender.valueOf(value))
        }
        catch (e: IllegalArgumentException) {
            it.copy(gender = Gender.Unknown)
        }
    }

    fun onCountryChange(value: String) = formState.update {
        it.copy(country = value)
    }



    Column(
        modifier = Modifier.background(MaterialTheme.colorScheme.background)
    ) {
        UserDataForm(
            formState = formState.collectAsState(),
            onFirstNameChange = { onFirstNameChange(it) },
            onLastNameChange = { onLastNameChange(it) },
            onBirthdayChange = { onBirthdateChange(it) },
            onDescriptionChange = { onDescriptionChange(it) },
            onGenderChange = { onGenderChange(it) },
            onCountryChange = { onCountryChange(it) }
        )
    }

}

@Composable
@Preview
fun EditUserProfileScreenPreview() {
    EditUserProfileScreen()
}