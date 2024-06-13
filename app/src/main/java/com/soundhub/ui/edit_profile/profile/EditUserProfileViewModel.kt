package com.soundhub.ui.edit_profile.profile

import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.soundhub.R
import com.soundhub.data.dao.UserDao
import com.soundhub.data.model.Gender
import com.soundhub.data.model.User
import com.soundhub.domain.usecases.user.UpdateUserUseCase
import com.soundhub.ui.components.forms.IUserDataFormState
import com.soundhub.ui.events.UiEvent
import com.soundhub.ui.states.UserFormState
import com.soundhub.ui.viewmodels.UiStateDispatcher
import com.soundhub.utils.UiText
import com.soundhub.utils.mappers.UserMapper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class EditUserProfileViewModel @Inject constructor(
    private val updateUserUseCase: UpdateUserUseCase,
    private val uiStateDispatcher: UiStateDispatcher,
    private val userDao: UserDao,
): ViewModel() {
    private val authorizedUser: MutableStateFlow<User?> = MutableStateFlow(null)
    private val _formState: MutableStateFlow<UserFormState> = MutableStateFlow(UserFormState())
    val formState: Flow<IUserDataFormState> = _formState.asStateFlow()

    var isLoading = MutableStateFlow(false)
        private set

    init { initState() }

    override fun onCleared() {
        super.onCleared()
        authorizedUser.update { null }
    }

    private fun initState() = viewModelScope.launch(Dispatchers.Main) {
        userDao.getCurrentUser()?.let { user ->
            authorizedUser.update { user }
            updateFormStateFromUser(user)
        }
    }

    fun hasStateChanges(): Boolean = authorizedUser.value?.let { user ->
        val mappedUser = UserMapper.impl.mergeUserWithFormState(_formState.value, user.copy())
        mappedUser != authorizedUser.value
    } ?: false

    private fun updateFormStateFromUser(user: User) = _formState.update {
        val formState: UserFormState = UserMapper.impl.toFormState(user.copy())
        formState.copy(languages = formState.languages.filter { it.isNotEmpty() }.toMutableList())
    }

    fun updateUser() = viewModelScope.launch(Dispatchers.IO) {
        isLoading.update { true }
        val updatedUser: User? = userDao.getCurrentUser()?.let { user ->
            UserMapper.impl.mergeUserWithFormState(_formState.value, user)
        }

        updatedUser?.let {
            updateUserUseCase(updatedUser)
                .onSuccess {
                    withContext(Dispatchers.Main) {
                        userDao.saveUser(updatedUser)
                        uiStateDispatcher.setAuthorizedUser(updatedUser)
                    }
                }
                .onFailure {
                    withContext(Dispatchers.Main) {
                        val toastText = UiText.StringResource(R.string.toast_update_error)
                        uiStateDispatcher.sendUiEvent(UiEvent.ShowToast(toastText))
                    }
                }
        }

        Log.d("EditUserProfileViewModel", "current user after update: $updatedUser")
        isLoading.update { false }
    }

    fun setFirstName(value: String) = _formState.update {
        it.copy(firstName = value)
    }

    fun setLastName(value: String) = _formState.update {
        it.copy(lastName = value)
    }

    fun setBirthday(value: LocalDate?) = _formState.update {
        it.copy(birthday = value)
    }

    fun setDescription(value: String) = _formState.update {
        it.copy(description = value)
    }

    fun setGender(value: String) = _formState.update {
        try {
            it.copy(gender = Gender.valueOf(value))
        }
        catch (e: IllegalArgumentException) {
            Log.e("EditUserProfileViewModel", "error: ${e.stackTraceToString()}")
            it.copy(gender = Gender.UNKNOWN)
        }
    }

    fun setCountry(value: String) = _formState.update {
        it.copy(country = value)
    }

    fun setCity(value: String) = _formState.update {
        it.copy(city = value)
    }

    fun setAvatar(avatarUri: Uri) = _formState.update {
        Log.d("EditUserProfileViewModel", "setAvatar[uri]: $avatarUri")
        it.copy(avatarUrl = avatarUri.toString())
    }

    fun setLanguages(languages: List<String>) = _formState.update {
        it.copy(languages = languages.toMutableList())
    }
}