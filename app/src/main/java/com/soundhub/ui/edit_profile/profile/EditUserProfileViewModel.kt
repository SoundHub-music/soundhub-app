package com.soundhub.ui.edit_profile.profile

import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.soundhub.data.dao.UserDao
import com.soundhub.data.model.Gender
import com.soundhub.data.model.User
import com.soundhub.domain.usecases.user.UpdateUserUseCase
import com.soundhub.ui.states.UserFormState
import com.soundhub.utils.mappers.UserMapper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class EditUserProfileViewModel @Inject constructor(
    private val updateUserUseCase: UpdateUserUseCase,
    private val userDao: UserDao,
): ViewModel() {
    private val authorizedUser: MutableStateFlow<User?> = MutableStateFlow(null)
    val formState: MutableStateFlow<UserFormState> = MutableStateFlow(UserFormState())

    var isLoading = MutableStateFlow(false)
        private set

    init {
        viewModelScope.launch {
            userDao.getCurrentUser()?.let { user ->
                authorizedUser.update { user }
                updateFormStateFromUser(user)
            }
        }
    }

    fun hasStateChanges(): Boolean = authorizedUser.value?.let { user ->
        val mappedUser = UserMapper.impl.mergeUserWithFormState(formState.value, user.copy())
        mappedUser != authorizedUser.value
    } ?: false

    private fun updateFormStateFromUser(user: User) = formState.update {
        UserMapper.impl.toFormState(user.copy())
    }

    fun updateUser() = viewModelScope.launch(Dispatchers.IO) {
        isLoading.update { true }
        val updatedUser: User? = userDao.getCurrentUser()?.let { user ->
            UserMapper.impl.mergeUserWithFormState(formState.value, user)
        }

        updateUserUseCase(updatedUser)

        Log.d("EditUserProfileViewModel", "current user after update: $updatedUser")
        isLoading.update { false }
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

    fun setAvatar(avatarUri: Uri) = formState.update {
        Log.d("EditUserProfileViewModel", "setAvatar[uri]: ${avatarUri.toString()}")
        it.copy(avatarUrl = avatarUri.toString())
    }

    fun setLanguages(languages: List<String>) = formState.update {
        it.copy(languages = languages)
    }
}