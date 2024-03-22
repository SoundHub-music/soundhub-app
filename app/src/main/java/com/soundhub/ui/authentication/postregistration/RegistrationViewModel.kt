package com.soundhub.ui.authentication.postregistration

import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.soundhub.Route
import com.soundhub.ui.viewmodels.UiStateDispatcher
import com.soundhub.data.model.Artist
import com.soundhub.data.model.Genre
import com.soundhub.data.repository.MusicRepository
import com.soundhub.UiEvent
import com.soundhub.data.datastore.UserCredsStore
import com.soundhub.data.model.Gender
import com.soundhub.data.model.User
import com.soundhub.data.repository.AuthRepository
import com.soundhub.ui.states.AuthFormState
import com.soundhub.ui.states.RegistrationState
import com.soundhub.utils.Constants
import com.soundhub.utils.UiText
import com.soundhub.utils.Validator
import com.soundhub.utils.mappers.UserMapper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.mapstruct.factory.Mappers
import java.lang.IllegalArgumentException
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class RegistrationViewModel @Inject constructor(
    private val musicRepository: MusicRepository,
    private val uiStateDispatcher: UiStateDispatcher,
    private val authRepository: AuthRepository,
    private val userCredsStore: UserCredsStore
): ViewModel() {
    var genres = MutableStateFlow<List<Genre>>(emptyList())
        private set
    var artists = MutableStateFlow<List<Artist>>(emptyList())
        private set
    var isLoading = MutableStateFlow(false)
        private set
    var chosenGenres = MutableStateFlow<List<Genre>>(emptyList())
        private set
    var chosenArtists = MutableStateFlow<List<Artist>>(emptyList())
        private set

    var registerState: MutableStateFlow<RegistrationState> = MutableStateFlow(RegistrationState())
        private set


    init {
        viewModelScope.launch {
            isLoading.value = true
            musicRepository.getAllGenres()
                .onSuccess {
                    genres.value = it.body?.genres ?: emptyList()
                }
                .onFailure {
                    uiStateDispatcher.sendUiEvent(
                        UiEvent.ShowToast(UiText.DynamicString(
                            it.errorBody?.detail ?: it.throwable?.message ?: ""
                        ))
                    )
                }
           isLoading.value = false
        }
    }

    override fun onCleared() {
        super.onCleared()
        Log.d("PostRegistrationViewModel", "viewmodel was cleared")
    }

    fun setChosenGenres(list: List<Genre>) = chosenGenres.update { list }

    fun setChosenGenres(genre: Genre) = chosenGenres.update {
        it + genre
    }

    fun setChosenArtists(list: List<Artist>) = chosenArtists.update { list }

    fun setChosenArtists(artist: Artist) = chosenArtists.update {
        it + artist
    }

    fun setFirstName(value: String) = registerState.update {
        it.copy(firstName = value, isFirstNameValid = value.isNotEmpty())
    }


    fun setLastName(value: String) = registerState.update {
        it.copy(lastName = value, isLastNameValid = value.isNotEmpty())
    }


    fun setBirthday(value: LocalDate?) = registerState.update {
        it.copy(birthday = value, isBirthdayValid = value != null)
    }


    fun setGender(value: String) {
        try {
            registerState.update {
                it.copy(gender = Gender.valueOf(value))
            }
        }
        catch (e: IllegalArgumentException) {
            Log.e("AuthViewModel", "setGender: ${e.message}")
            registerState.update {
                it.copy(gender = Gender.UNKNOWN)
            }
        }
    }

    fun setCountry(value: String) = registerState.update { it.copy(country = value) }
    fun setCity(value: String) = registerState.update { it.copy(city = value) }
    fun setDescription(value: String) = registerState.update { it.copy(description = value) }

    fun setLanguages(languages: List<String>) = registerState.update {
        it.copy(languages = languages.toMutableList())
    }

    fun setAvatar(avatarUrl: Uri?) = registerState.update {
        it.copy(avatarUrl = avatarUrl?.path)
    }

    fun onPostRegisterNextBtnClick(currentRoute: Route) {
        Log.d(
            Constants.LOG_REGISTER_STATE,
            "AuthenticationViewModel[onPostRegisterNextButtonClick]: ${registerState.value}"
        )
        when (currentRoute) {
            is Route.Authentication.ChooseGenres -> onEvent(RegistrationEvent.OnChooseArtists)
            is Route.Authentication.ChooseArtists -> onEvent(RegistrationEvent.OnFillUserData)
            is Route.Authentication.FillUserData -> {
                registerState.update {
                    it.copy(
                        isFirstNameValid = it.firstName?.isNotEmpty() ?: false,
                        isLastNameValid = it.lastName?.isNotEmpty() ?: false,
                        isBirthdayValid = it.birthday != null
                    )
                }

                if (Validator.validateRegistrationState(registerState.value)) {
                    val userMapper: UserMapper = Mappers.getMapper(UserMapper::class.java)
                    val user: User = userMapper.fromRegistrationState(registerState.value)
                    onEvent(RegistrationEvent.OnRegister(user))
                }
            }
            else -> Unit
        }
    }

    fun signUp(authForm: AuthFormState) {
        Log.d("PostRegistrationViewModel", "authForm: $authForm")
        registerState.update {
            it.copy(
                email = authForm.email,
                password = authForm.password
            )
        }
        onEvent(RegistrationEvent.OnChooseGenres)
    }

    private fun onEvent(event: RegistrationEvent) {
        when (event) {
            is RegistrationEvent.OnRegister -> viewModelScope.launch {
                val userMapper: UserMapper = Mappers.getMapper(UserMapper::class.java)
                val registerRequestBody = userMapper.registerStateToRegisterRequestBody(registerState.value)
                authRepository
                    .signUp(registerRequestBody)
                    .onSuccess {
                        userCredsStore.updateCreds(it.body)
                        uiStateDispatcher.sendUiEvent(UiEvent.UpdateUserInstance(event.user))
                        uiStateDispatcher.sendUiEvent(UiEvent.Navigate(Route.Postline))
                    }
                    .onFailure {
                        uiStateDispatcher.sendUiEvent(
                            UiEvent.ShowToast(
                                UiText.DynamicString(it.errorBody?.detail ?: it.throwable?.message ?: "")
                            )
                        )
                    }
            }

            is RegistrationEvent.OnChooseGenres ->
                uiStateDispatcher.sendUiEvent(UiEvent.Navigate(Route.Authentication.ChooseGenres))

            is RegistrationEvent.OnChooseArtists ->
                uiStateDispatcher.sendUiEvent(UiEvent.Navigate(Route.Authentication.ChooseArtists))

            is RegistrationEvent.OnFillUserData ->
                uiStateDispatcher.sendUiEvent(UiEvent.Navigate(Route.Authentication.FillUserData))
        }
    }
}