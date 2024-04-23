package com.soundhub.ui.edit_profile

import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.soundhub.ui.events.UiEvent
import com.soundhub.data.datastore.UserCredsStore
import com.soundhub.data.datastore.UserPreferences
import com.soundhub.data.model.Gender
import com.soundhub.data.model.User
import com.soundhub.data.repository.UserRepository
import com.soundhub.ui.states.UserFormState
import com.soundhub.ui.viewmodels.UiStateDispatcher
import com.soundhub.utils.UiText
import com.soundhub.utils.mappers.UserMapper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.mapstruct.factory.Mappers
import java.lang.IllegalArgumentException
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class EditUserProfileViewModel @Inject constructor(
    private val userCredsStore: UserCredsStore,
    private val userRepository: UserRepository,
    private val uiStateDispatcher: UiStateDispatcher
): ViewModel() {
    var formState: MutableStateFlow<UserFormState> = MutableStateFlow(UserFormState())
        private set

    private val userCreds: MutableStateFlow<UserPreferences?> = MutableStateFlow(null)
    private val currentUser: MutableStateFlow<User?> = MutableStateFlow(null)
    var isLoading = MutableStateFlow(false)
        private set

    init {
        viewModelScope.launch {
            userCreds.update { userCredsStore.getCreds().firstOrNull() }
        }
    }

    fun setUser(user: User?) {
        val userMapper: UserMapper = Mappers.getMapper(UserMapper::class.java)
        currentUser.update { user }

        val userFormState: UserFormState = userMapper.toFormState(user)
        formState.update { userFormState }
    }

    fun updateUser() = viewModelScope.launch {
        isLoading.update { true }
        val userMapper: UserMapper = Mappers.getMapper(UserMapper::class.java)
        currentUser.update {
            userMapper.mergeUserWithFormState(
                formState.value,
                currentUser.value
            )
        }

        Log.d("EditUserProfileViewModel", "current user after update: ${currentUser.value}")

        userRepository.updateUserById(
            user = currentUser.value,
            accessToken = userCreds.value?.accessToken
        )
            .onSuccess {
                it.body?.let { user ->
                    uiStateDispatcher.sendUiEvent(UiEvent.UpdateUserInstance(user))
                }
            }
            .onFailure {
                uiStateDispatcher.sendUiEvent(
                    UiEvent
                    .ShowToast(UiText.DynamicString("update error")))
            }
            .finally {
                isLoading.update { false }
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