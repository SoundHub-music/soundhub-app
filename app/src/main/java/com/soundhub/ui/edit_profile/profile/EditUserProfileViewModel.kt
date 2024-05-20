package com.soundhub.ui.edit_profile.profile

import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.soundhub.data.datastore.UserCredsStore
import com.soundhub.data.datastore.UserPreferences
import com.soundhub.data.model.Gender
import com.soundhub.data.model.User
import com.soundhub.domain.usecases.user.UpdateUserUseCase
import com.soundhub.ui.states.UserFormState
import com.soundhub.ui.viewmodels.UiStateDispatcher
import com.soundhub.utils.mappers.UserMapper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.lang.IllegalArgumentException
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class EditUserProfileViewModel @Inject constructor(
    private val uiStateDispatcher: UiStateDispatcher,
    private val updateUserUseCase: UpdateUserUseCase,
    userCredsStore: UserCredsStore
): ViewModel() {
    private val userCreds: Flow<UserPreferences> = userCredsStore.getCreds()
    private val currentUser: MutableStateFlow<User?> = MutableStateFlow(null)
    val formState: MutableStateFlow<UserFormState> = MutableStateFlow(UserFormState())
    var isLoading = MutableStateFlow(false)
        private set

    init {
        viewModelScope.launch {
            val userFromState: Flow<User?> = uiStateDispatcher.uiState.map { it.authorizedUser }
            userFromState.collect { authorizedUser ->
                authorizedUser?.let { user ->
                    currentUser.update { user }
                    updateFormStateFromUser(user)
                }
            }
        }
    }

    private fun updateFormStateFromUser(user: User) = formState.update {
        UserMapper.impl.toFormState(user)
    }

    fun updateUser() = viewModelScope.launch(Dispatchers.IO) {
        isLoading.update { true }
        updateLocalUserStateFromFormState()
        updateUserUseCase(
            accessToken = userCreds.firstOrNull()?.accessToken,
            user = currentUser.value
        )

        Log.d("EditUserProfileViewModel", "current user after update: ${currentUser.value}")
        isLoading.update { false }
    }

    private fun updateLocalUserStateFromFormState() = currentUser.value?.let { user ->
        currentUser.update {
            UserMapper.impl
                .mergeUserWithFormState(formState.value, user)
        }
    }

    fun setFirstName(value: String) = formState.update {
        it.copy(firstName = value)
    }

    fun setLastName(value: String) = formState.update {
        it.copy(lastName = value)
    }

    fun setBirthday(value: LocalDate?) = formState.update {
        it.copy(birthday = value)
    }

    fun setDescription(value: String) = formState.update {
        it.copy(description = value)
    }

    fun setGender(value: String) = formState.update {
        try {
            it.copy(gender = Gender.valueOf(value))
        }
        catch (e: IllegalArgumentException) {
            Log.e("EditUserProfileViewModel", "error: ${e.stackTraceToString()}")
            it.copy(gender = Gender.UNKNOWN)
        }
    }

    fun setCountry(value: String) = formState.update {
        it.copy(country = value)
    }

    fun setCity(value: String) = formState.update {
        it.copy(city = value)
    }

    fun setAvatar(avatarUri: Uri?) = formState.update {
        it.copy(avatarUrl = avatarUri?.toString())
    }

    fun setLanguages(languages: List<String>) = formState.update {
        it.copy(languages = languages)
    }
}