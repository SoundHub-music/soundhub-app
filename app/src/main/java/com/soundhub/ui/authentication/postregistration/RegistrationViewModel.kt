package com.soundhub.ui.authentication.postregistration

import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.soundhub.Route
import com.soundhub.ui.viewmodels.UiStateDispatcher
import com.soundhub.data.model.Artist
import com.soundhub.data.model.Genre
import com.soundhub.ui.events.UiEvent
import com.soundhub.data.api.requests.RegisterRequestBody
import com.soundhub.data.dao.UserDao
import com.soundhub.data.database.AppDatabase
import com.soundhub.data.datastore.UserCredsStore
import com.soundhub.data.model.Gender
import com.soundhub.data.model.User
import com.soundhub.data.repository.AuthRepository
import com.soundhub.domain.usecases.music.LoadArtistsUseCase
import com.soundhub.domain.usecases.music.LoadGenresUseCase
import com.soundhub.domain.usecases.music.SearchArtistsUseCase
import com.soundhub.ui.authentication.postregistration.states.ArtistUiState
import com.soundhub.ui.authentication.postregistration.states.GenreUiState
import com.soundhub.ui.authentication.states.AuthFormState
import com.soundhub.ui.authentication.postregistration.states.RegistrationState
import com.soundhub.ui.states.UiState
import com.soundhub.utils.UiText
import com.soundhub.utils.Validator
import com.soundhub.utils.mappers.RegisterDataMapper
import com.soundhub.utils.mappers.UserMapper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.lang.IllegalArgumentException
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class RegistrationViewModel @Inject constructor(
    private val uiStateDispatcher: UiStateDispatcher,
    private val authRepository: AuthRepository,
    private val userCredsStore: UserCredsStore,
    private val loadGenresUseCase: LoadGenresUseCase,
    private val loadArtistsUseCase: LoadArtistsUseCase,
    private val searchArtistsUseCase: SearchArtistsUseCase,
    appDb: AppDatabase
): ViewModel() {
    private val uiState: StateFlow<UiState> = uiStateDispatcher.uiState.asStateFlow()
    private val userDao: UserDao = appDb.userDao()

    val genreUiState: MutableStateFlow<GenreUiState> = MutableStateFlow(GenreUiState())
    val artistUiState: MutableStateFlow<ArtistUiState> = MutableStateFlow(ArtistUiState())
    val registerState: MutableStateFlow<RegistrationState> = MutableStateFlow(RegistrationState())

    private var searchJob: Job? = null
    private var loadArtistsJob: Job? = null

    init { viewModelScope.launch { loadGenres() } }

    override fun onCleared() {
        super.onCleared()
        Log.d("PostRegistrationViewModel", "viewmodel was cleared")
    }

    private suspend fun loadGenres() = viewModelScope.launch(Dispatchers.IO) {
        loadGenresUseCase(
            countPerPage = 50,
            genreUiState = genreUiState
        )
    }

    suspend fun loadArtists(page: Int = 1) {
        loadArtistsJob = viewModelScope.launch(Dispatchers.IO) {
            loadArtistsUseCase(
                genreUiState = genreUiState,
                artistUiState = artistUiState,
                page = page
            )
        }
    }

    fun searchArtists(value: String) {
        searchJob?.cancel()
        searchJob = viewModelScope.launch(Dispatchers.IO) {
            searchArtistsUseCase(
                searchBarValue = value,
                artistStateFlow = artistUiState
            )
        }
    }

    fun onPostRegisterNextBtnClick() {
        Log.d("RegistrationViewModel", "onPostRegisterNextButtonClick: ${registerState.value}")
        val uiState = uiState.value
        when (uiState.currentRoute) {
            Route.Authentication.ChooseGenres.route -> handleChooseGenres()
            Route.Authentication.ChooseArtists.route -> handleChooseArtists()
            Route.Authentication.FillUserData.route -> handleFillUserData()
            else -> Unit
        }
    }

    private fun handleChooseGenres() = viewModelScope.launch(Dispatchers.IO) {
        registerState.update {
            it.copy(favoriteGenres = genreUiState.value.chosenGenres)
        }
        loadArtists()
        onEvent(RegistrationEvent.OnChooseArtists)
    }

    private fun handleChooseArtists() {
        registerState.update {
            it.copy(favoriteArtists = artistUiState.value.chosenArtists)
        }
        onEvent(RegistrationEvent.OnFillUserData)
    }

    private fun handleFillUserData() {
        loadArtistsJob?.cancel()
        registerState.update {
            it.copy(
                isFirstNameValid = it.firstName?.isNotEmpty() ?: false,
                isLastNameValid = it.lastName?.isNotEmpty() ?: false,
                isBirthdayValid = it.birthday != null
            )
        }

        if (Validator.validateRegistrationState(registerState.value)) {
            val user: User = UserMapper.impl.fromRegistrationState(registerState.value)
            user.favoriteArtistsIds = user.favoriteArtists.map { it.id }

            onEvent(RegistrationEvent.OnRegister(user))
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
            is RegistrationEvent.OnRegister -> handleRegister(event)
            is RegistrationEvent.OnChooseGenres -> handleNavigateToChooseGenres()
            is RegistrationEvent.OnChooseArtists -> handleNavigateToChooseArtists()
            is RegistrationEvent.OnFillUserData -> handleNavigateToFillUserData()
        }
    }

    private fun handleNavigateToChooseGenres() = viewModelScope.launch(Dispatchers.IO) {
        uiStateDispatcher.sendUiEvent(UiEvent.Navigate(Route.Authentication.ChooseGenres))
    }

    private fun handleNavigateToChooseArtists() = viewModelScope.launch {
        uiStateDispatcher.sendUiEvent(UiEvent.Navigate(Route.Authentication.ChooseArtists))
    }

    private fun handleNavigateToFillUserData() = viewModelScope.launch {
        uiStateDispatcher.sendUiEvent(UiEvent.Navigate(Route.Authentication.FillUserData))
    }

    private fun handleRegister(event: RegistrationEvent.OnRegister) = viewModelScope.launch(Dispatchers.IO) {
        val registerRequestBody: RegisterRequestBody = RegisterDataMapper.impl
            .registerStateToRegisterRequestBody(registerState.value)

        authRepository
            .signUp(registerRequestBody)
            .onSuccess { response ->
                userCredsStore.updateCreds(response.body)
                userDao.saveUser(event.user)
                uiStateDispatcher.setAuthorizedUser(event.user)

                uiStateDispatcher.sendUiEvent(UiEvent.Navigate(Route.Postline))
            }
            .onFailure {
                it.errorBody.detail?.let { error ->
                    val toastText: UiText.DynamicString = UiText.DynamicString(error)
                    uiStateDispatcher.sendUiEvent(UiEvent.ShowToast(toastText))
                }
            }
    }

    fun addChosenGenre(list: List<Genre>) = genreUiState.update { it.copy(chosenGenres = list) }

    fun addChosenGenre(genre: Genre) = genreUiState.update {
        it.copy(chosenGenres =  it.chosenGenres + genre)
    }

    fun addChosenArtist(list: List<Artist>) = artistUiState.update { it.copy(chosenArtists = list) }

    fun addChosenArtist(artist: Artist) = artistUiState.update {
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
            Log.e("RegistrationViewModel", "setGender: ${e.message}")
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

    fun setCurrentArtistPage(page: Int) = artistUiState.update {
        it.copy(currentPage = page)
    }
}