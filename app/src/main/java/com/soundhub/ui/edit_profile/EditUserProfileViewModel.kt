package com.soundhub.ui.edit_profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.soundhub.data.datastore.UserStore
import com.soundhub.data.model.Gender
import com.soundhub.ui.edit_profile.state.EditDataFormState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.lang.IllegalArgumentException
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class EditUserProfileViewModel @Inject constructor(
    userStore: UserStore
): ViewModel() {
    var formState = MutableStateFlow(EditDataFormState())
        private set

    init {
        viewModelScope.launch {
            formState.update { EditDataFormState(userStore.getUser().firstOrNull()) }
        }
    }

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
}