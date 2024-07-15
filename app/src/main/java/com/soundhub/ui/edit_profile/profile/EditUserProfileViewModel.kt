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
import com.soundhub.data.states.UserFormState
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
    private val _formState: MutableStateFlow<UserFormState> = MutableStateFlow(UserFormState())
    val formState: Flow<IUserDataFormState> = _formState.asStateFlow()

    private val _isDialogOpened = MutableStateFlow(false)
    val isDialogOpened: Flow<Boolean> = _isDialogOpened.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    init { initState() }


    private fun initState() = viewModelScope.launch(Dispatchers.Main) {
        userDao.getCurrentUser()?.let { user -> updateFormStateFromUser(user) }
    }

    suspend fun hasStateChanges(): Boolean {
        val authorizedUser: User? = userDao.getCurrentUser()

        return authorizedUser?.let {
            val mappedUser = UserMapper.impl.mergeUserWithFormState(
                _formState.value,
                authorizedUser.copy(
                    languages = it.languages.toMutableList(),
                    friends = it.friends.map { f -> f.copy() },
                    favoriteArtistsIds = it.favoriteArtistsIds.toList(),
                    favoriteArtists = it.favoriteArtists.map { a -> a.copy() },
                    favoriteGenres = it.favoriteGenres.map { g -> g.copy() }
                )
            )

            return mappedUser != authorizedUser
        } ?: false
    }

    private fun updateFormStateFromUser(user: User) = _formState.update {
        val formState: UserFormState = UserMapper.impl.toFormState(user.copy())
        formState.copy(
            languages = formState.languages.filter { it.isNotEmpty() }.toMutableList(),
        )
    }

    private fun updateUser() = viewModelScope.launch(Dispatchers.IO) {
        _isLoading.update { true }
        var toastText = UiText.StringResource(R.string.toast_profile_edited_successfully)

        val updatedUser: User? = userDao.getCurrentUser()?.let { user ->
            UserMapper.impl.mergeUserWithFormState(_formState.value, user)
        }?.also { user ->
            updateUserUseCase(user)
                .onSuccess {
                    withContext(Dispatchers.Main) {
                        userDao.saveUser(user)
                        with(uiStateDispatcher) {
                            setAuthorizedUser(user)
                            sendUiEvent(UiEvent.ShowToast(toastText))
                        }
                    }
                }
                .onFailure {
                    withContext(Dispatchers.Main) {
                        toastText = UiText.StringResource(R.string.toast_update_error)
                        uiStateDispatcher.sendUiEvent(UiEvent.ShowToast(toastText))
                    }
                }
        }

        Log.d("EditUserProfileViewModel", "current user after update: $updatedUser")
        _isLoading.update { false }
    }

    fun onTopNavigationButtonClick() = viewModelScope.launch(Dispatchers.Main) {
        if (hasStateChanges())
            _isDialogOpened.update { true }
        else uiStateDispatcher.sendUiEvent(UiEvent.PopBackStack)
    }

    fun onSaveChangesButtonClick() = viewModelScope.launch(Dispatchers.Main) {
        updateUser().also {
            uiStateDispatcher.sendUiEvent(UiEvent.PopBackStack)
        }
    }

    fun setDialogVisibility(state: Boolean) = _isDialogOpened.update { state }

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