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
import com.soundhub.ui.events.UiEvent
import com.soundhub.data.api.requests.RegisterRequestBody
import com.soundhub.data.datastore.UserCredsStore
import com.soundhub.data.model.Gender
import com.soundhub.data.model.User
import com.soundhub.data.repository.AuthRepository
import com.soundhub.ui.authentication.postregistration.states.ArtistUiState
import com.soundhub.ui.authentication.postregistration.states.GenreUiState
import com.soundhub.ui.authentication.states.AuthFormState
import com.soundhub.ui.authentication.postregistration.states.RegistrationState
import com.soundhub.utils.UiText
import com.soundhub.utils.Validator
import com.soundhub.utils.mappers.RegisterDataMapper
import com.soundhub.utils.mappers.UserMapper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
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
    var genreUiState: MutableStateFlow<GenreUiState> = MutableStateFlow(GenreUiState())
        private set
    var artistUiState: MutableStateFlow<ArtistUiState> = MutableStateFlow(ArtistUiState())
        private set
    var registerState: MutableStateFlow<RegistrationState> = MutableStateFlow(RegistrationState())
        private set


    init {
        viewModelScope.launch {
            loadGenres()
        }
    }

    private suspend fun loadGenres() = viewModelScope.launch(Dispatchers.IO) {
        musicRepository.getAllGenres(50)
            .onSuccess { successResponse ->
                genreUiState.update {
                    it.copy(
                        status = successResponse.status,
                        genres = successResponse.body ?: emptyList()
//                            ?.topTags
//                            ?.tag
//                            ?.map { tag -> Genre(name = tag.name) }
//                            ?: emptyList()
                    )
                }
            }
            .onFailure {
                it.errorBody.detail?.let { error ->
                    uiStateDispatcher.sendUiEvent(
                        UiEvent.ShowToast(UiText.DynamicString(error))
                    )
                }
            }
    }

    private suspend fun loadArtists() = viewModelScope.launch(Dispatchers.IO) {
        musicRepository.loadArtistByGenresToState(
            genres = genreUiState.value.chosenGenres.map { it.name ?: "" },
            styles = genreUiState.value.chosenGenres.map { it.name ?: "" },
            artistState = artistUiState
        )
        .finally { response ->
            artistUiState.update {
                it.copy(status = response.status)
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        Log.d("PostRegistrationViewModel", "viewmodel was cleared")
    }

    fun setChosenGenres(list: List<Genre>) = genreUiState.update { it.copy(chosenGenres = list) }

    fun setChosenGenres(genre: Genre) = genreUiState.update {
        it.copy(chosenGenres =  it.chosenGenres + genre)
    }

    fun setChosenArtists(list: List<Artist>) = artistUiState.update { it.copy(chosenArtists = list) }

    fun setChosenArtists(artist: Artist) = artistUiState.update {
        it.copy(chosenArtists = it.chosenArtists + artist)
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
        it.copy(avatarUrl = avatarUrl?.toString())
    }

    fun onPostRegisterNextBtnClick(currentRoute: Route) {
        Log.d(
            "RegistrationViewModel",
            "onPostRegisterNextButtonClick: ${registerState.value}"
        )
        when (currentRoute) {
            is Route.Authentication.ChooseGenres -> viewModelScope.launch(Dispatchers.IO) {
                registerState.update {
                    it.copy(favoriteGenres = genreUiState.value.chosenGenres)
                }
                loadArtists()
                onEvent(RegistrationEvent.OnChooseArtists)
            }
            is Route.Authentication.ChooseArtists -> {
                registerState.update {
                    it.copy(favoriteArtists = artistUiState.value.chosenArtists)
                }
                onEvent(RegistrationEvent.OnFillUserData)
            }
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

    fun onSignUpButtonClick(authForm: AuthFormState) {
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
                val registerDataMapper: RegisterDataMapper = Mappers
                    .getMapper(RegisterDataMapper::class.java)

                val registerRequestBody: RegisterRequestBody = registerDataMapper
                    .registerStateToRegisterRequestBody(registerState.value)

                authRepository
                    .signUp(registerRequestBody)
                    .onSuccess {
                        userCredsStore.updateCreds(it.body)
                        uiStateDispatcher.sendUiEvent(UiEvent.UpdateUserInstance(event.user))
                        uiStateDispatcher.sendUiEvent(UiEvent.Navigate(Route.Postline))
                    }
                    .onFailure {
                        it.errorBody.detail?.let { error ->
                            uiStateDispatcher.sendUiEvent(
                                UiEvent.ShowToast(
                                    UiText.DynamicString(error)
                                )
                            )
                        }
                    }
            }

            is RegistrationEvent.OnChooseGenres -> viewModelScope.launch {
                uiStateDispatcher.sendUiEvent(UiEvent.Navigate(Route.Authentication.ChooseGenres))
            }

            is RegistrationEvent.OnChooseArtists -> viewModelScope.launch {
                uiStateDispatcher.sendUiEvent(UiEvent.Navigate(Route.Authentication.ChooseArtists))
            }

            is RegistrationEvent.OnFillUserData -> viewModelScope.launch {
                uiStateDispatcher.sendUiEvent(UiEvent.Navigate(Route.Authentication.FillUserData))
            }
        }
    }
}