package com.soundhub.ui.edit_profile

import androidx.lifecycle.ViewModel
import com.soundhub.data.model.Gender
import com.soundhub.data.model.User
import com.soundhub.ui.states.UserFormState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import java.lang.IllegalArgumentException
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class EditUserProfileViewModel @Inject constructor(): ViewModel() {
    var formState = MutableStateFlow(UserFormState())
        private set

    fun setUser(user: User?) = formState.update { UserFormState(user) }
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
            it.copy(gender = Gender.UNKNOWN)
        }
    }

    fun onCountryChange(value: String) = formState.update {
        it.copy(country = value)
    }
}