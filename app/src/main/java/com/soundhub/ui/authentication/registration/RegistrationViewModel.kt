package com.soundhub.ui.authentication.registration

import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.soundhub.Route
import com.soundhub.Route.Authentication
import com.soundhub.ui.viewmodels.UiStateDispatcher
import com.soundhub.data.model.Artist
import com.soundhub.data.model.Genre
import com.soundhub.ui.events.UiEvent
import com.soundhub.data.api.requests.RegisterRequestBody
import com.soundhub.data.dao.UserDao
import com.soundhub.data.database.AppDatabase
import com.soundhub.data.datastore.UserCredsStore
import com.soundhub.data.datastore.UserPreferences
import com.soundhub.data.model.Gender
import com.soundhub.data.model.User
import com.soundhub.data.repository.AuthRepository
import com.soundhub.domain.usecases.music.LoadArtistsUseCase
import com.soundhub.domain.usecases.music.LoadGenresUseCase
import com.soundhub.domain.usecases.music.SearchArtistsUseCase
import com.soundhub.ui.authentication.registration.states.ArtistUiState
import com.soundhub.ui.authentication.registration.states.GenreUiState
import com.soundhub.ui.authentication.AuthFormState
import com.soundhub.ui.authentication.registration.states.RegistrationState
import com.soundhub.ui.components.forms.IUserDataFormState
import com.soundhub.ui.events.UiEvent.*
import com.soundhub.utils.Validator
import com.soundhub.utils.mappers.RegisterDataMapper
import com.soundhub.utils.mappers.UserMapper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
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
    private val uiState = uiStateDispatcher.uiState
    private val userDao: UserDao = appDb.userDao()

    private val _genreUiState: MutableStateFlow<GenreUiState> = MutableStateFlow(GenreUiState())
    val genreUiState = _genreUiState.asStateFlow()

    private val _artistUiState: MutableStateFlow<ArtistUiState> = MutableStateFlow(ArtistUiState())
    val artistUiState = _artistUiState.asStateFlow()

    private val _registerState: MutableStateFlow<RegistrationState> = MutableStateFlow(RegistrationState())
    val registerState: Flow<IUserDataFormState> = _registerState.asStateFlow()

    private var searchJob: Job? = null
    private var loadArtistsJob = MutableStateFlow<List<Job?>>(emptyList())

    init { viewModelScope.launch { loadGenres() } }

    override fun onCleared() {
        super.onCleared()
        Log.d("PostRegistrationViewModel", "viewmodel was cleared")
    }

    private suspend fun loadGenres() = viewModelScope.launch(Dispatchers.IO) {
        loadGenresUseCase(
            countPerPage = 50,
            genreUiState = _genreUiState
        )
    }

    suspend fun loadArtists(page: Int = 1) {
        val job = viewModelScope.launch(Dispatchers.IO) {
            loadArtistsUseCase(
                genreUiState = _genreUiState,
                artistUiState = _artistUiState,
                page = page
            )
        }

        loadArtistsJob.update { it + job }
    }

    fun searchArtists(value: String) {
        searchJob?.cancel()
        searchJob = viewModelScope.launch(Dispatchers.IO) {
            searchArtistsUseCase(
                searchBarValue = value,
                artistStateFlow = _artistUiState
            )
        }
    }

    fun onPostRegisterNextBtnClick() = viewModelScope.launch(Dispatchers.Main) {
        Log.d("RegistrationViewModel", "onPostRegisterNextButtonClick: ${_registerState.value}")
        val uiState = uiState.firstOrNull()
        when (uiState?.currentRoute) {
            Authentication.ChooseGenres.route -> handleChooseGenres()
            Authentication.ChooseArtists.route -> handleChooseArtists()
            Authentication.FillUserData.route -> handleFillUserData()
            else -> Unit
        }
    }

    private fun handleChooseGenres() = viewModelScope.launch(Dispatchers.Main) {
        _registerState.update {
            it.copy(favoriteGenres = _genreUiState.value.chosenGenres)
        }
        loadArtists()
        uiStateDispatcher.sendUiEvent(Navigate(Authentication.ChooseArtists))
    }

    private fun handleChooseArtists() = viewModelScope.launch(Dispatchers.Main) {
        _registerState.update {
            it.copy(favoriteArtists = _artistUiState.value.chosenArtists)
        }
        uiStateDispatcher.sendUiEvent(Navigate(Authentication.FillUserData))
    }

    private fun handleFillUserData() = viewModelScope.launch(Dispatchers.Main) {
        loadArtistsJob.firstOrNull()
            .orEmpty()
            .forEach { j -> j?.cancel() }

        searchJob?.cancel()
        _registerState.update {
            it.copy(
                isFirstNameValid = it.firstName.isNotEmpty(),
                isLastNameValid = it.lastName.isNotEmpty(),
                isBirthdayValid = it.birthday != null
            )
        }

        if (Validator.validateRegistrationState(_registerState.value)) {
            val user: User = UserMapper.impl.fromRegistrationState(_registerState.value)
            user.favoriteArtistsIds = user.favoriteArtists.map { it.id }

            handleRegister(user)
        }
    }


    fun onSignUpButtonClick(authForm: AuthFormState) = viewModelScope.launch(Dispatchers.Main) {
        Log.d("PostRegistrationViewModel", "authForm: $authForm")
        _registerState.update {
            it.copy(
                email = authForm.email,
                password = authForm.password
            )
        }

        uiStateDispatcher.sendUiEvent(Navigate(Authentication.ChooseGenres))
    }


    private fun handleRegister(user: User) = viewModelScope.launch(Dispatchers.IO) {
        val registerRequestBody: RegisterRequestBody = RegisterDataMapper.impl
            .registerStateToRegisterRequestBody(_registerState.value)

        authRepository
            .signUp(registerRequestBody)
            .onSuccessWithContext(Dispatchers.IO) { response ->
                saveUserCreds(response.body, user)
            }
            .onFailureWithContext { error ->
                val errorEvent: UiEvent = Error(error.errorBody, error.throwable)
                uiStateDispatcher.sendUiEvent(errorEvent)
            }
    }

    private suspend fun saveUserCreds(
        userCreds: UserPreferences?,
        user: User
    ) {
        userCredsStore.updateCreds(userCreds)
        userDao.saveUser(user)

        with(uiStateDispatcher) {
            setAuthorizedUser(user)
            sendUiEvent(Navigate(Route.PostLine))
        }
    }

    fun addChosenGenre(list: List<Genre>) = _genreUiState.update { it.copy(chosenGenres = list) }

    fun addChosenGenre(genre: Genre) = _genreUiState.update {
        it.copy(chosenGenres =  it.chosenGenres + genre)
    }

    fun addChosenArtist(list: List<Artist>) = _artistUiState.update { it.copy(chosenArtists = list) }

    fun addChosenArtist(artist: Artist) = _artistUiState.update {
        it.copy(chosenArtists = it.chosenArtists + artist)
    }

    fun setFirstName(value: String) = _registerState.update {
        it.copy(firstName = value, isFirstNameValid = value.isNotEmpty())
    }


    fun setLastName(value: String) = _registerState.update {
        it.copy(lastName = value, isLastNameValid = value.isNotEmpty())
    }


    fun setBirthday(value: LocalDate?) = _registerState.update {
        it.copy(birthday = value, isBirthdayValid = value != null)
    }


    fun setGender(value: String) {
        try {
            _registerState.update {
                it.copy(gender = Gender.valueOf(value))
            }
        }
        catch (e: IllegalArgumentException) {
            Log.e("RegistrationViewModel", "setGender: ${e.message}")
            _registerState.update {
                it.copy(gender = Gender.UNKNOWN)
            }
        }
    }

    fun setCountry(value: String) = _registerState.update { it.copy(country = value) }
    fun setCity(value: String) = _registerState.update { it.copy(city = value) }
    fun setDescription(value: String) = _registerState.update { it.copy(description = value) }

    fun setLanguages(languages: List<String>) = _registerState.update {
        it.copy(languages = languages.toMutableList())
    }

    fun setAvatar(avatarUrl: Uri?) = _registerState.update {
        it.copy(avatarUrl = avatarUrl?.toString())
    }

    fun setCurrentArtistPage(page: Int) = _artistUiState.update {
        it.copy(currentPage = page)
    }
}