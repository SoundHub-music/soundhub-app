package com.soundhub.ui.edit_profile.music

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.soundhub.Route
import com.soundhub.data.datastore.UserCredsStore
import com.soundhub.data.datastore.UserPreferences
import com.soundhub.data.model.Artist
import com.soundhub.data.model.Genre
import com.soundhub.data.model.User
import com.soundhub.domain.usecases.music.LoadArtistsUseCase
import com.soundhub.domain.usecases.music.LoadGenresUseCase
import com.soundhub.domain.usecases.music.SearchArtistsUseCase
import com.soundhub.domain.usecases.user.UpdateUserUseCase
import com.soundhub.ui.authentication.postregistration.states.ArtistUiState
import com.soundhub.ui.authentication.postregistration.states.GenreUiState
import com.soundhub.ui.events.UiEvent
import com.soundhub.ui.states.UiState
import com.soundhub.ui.viewmodels.UiStateDispatcher
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditMusicPreferencesViewModel @Inject constructor(
    private val uiStateDispatcher: UiStateDispatcher,
    private val updateUserUseCase: UpdateUserUseCase,
    private val loadGenresUseCase: LoadGenresUseCase,
    private val loadArtistsUseCase: LoadArtistsUseCase,
    private val searchArtistsUseCase: SearchArtistsUseCase,
    userCredsStore: UserCredsStore
): ViewModel() {
    val genreUiState: MutableStateFlow<GenreUiState> = MutableStateFlow(GenreUiState())
    val artistUiState: MutableStateFlow<ArtistUiState> = MutableStateFlow(ArtistUiState())

    private val uiState: StateFlow<UiState> = uiStateDispatcher.uiState.asStateFlow()
    private val userCreds: Flow<UserPreferences> = userCredsStore.getCreds()
    private val authorizedUser: Flow<User?> = uiState.map { it.authorizedUser }

    private var loadArtistsJob: Job? = null
    private var searchArtistsJob: Job? = null

    init { loadGenres() }

    override fun onCleared() {
        super.onCleared()
        Log.d("EditMusicPreferencesViewModel", "viewmodel was cleared")
    }

    fun onNextButtonClick() = viewModelScope.launch {
        val state: UiState? = uiState.firstOrNull()
        when (state?.currentRoute) {
            Route.EditFavoriteGenres.route -> handleFavoriteGenres()
            Route.EditFavoriteArtists.route -> handleFavoriteArtists()
            else -> Unit
        }
    }

    // TODO: fix bug when loadGenres calls outside the edit genres page
    private fun loadGenres() = viewModelScope.launch(Dispatchers.IO) {
        authorizedUser.collect { user ->
            val favoriteGenres: List<Genre> = user
                ?.favoriteGenres ?: emptyList()

            if (genreUiState.value.chosenGenres.isEmpty())
                genreUiState.update { it.copy(chosenGenres = favoriteGenres) }
            if (genreUiState.value.genres.isEmpty())
                loadGenresUseCase(genreUiState = genreUiState)
            Log.d("EditMusicPreferencesViewModel", "loadGenres: ${genreUiState.value}")
        }
    }

    fun loadArtists(page: Int = 1) {
        loadArtistsJob = viewModelScope.launch(Dispatchers.IO) {
            authorizedUser.collect { user ->
                val favoriteArtists: List<Artist> = user
                    ?.favoriteArtists ?: emptyList()

                loadArtistsUseCase(
                    artistUiState = artistUiState,
                    genreUiState = genreUiState,
                    page = page
                )
                artistUiState.update { it.copy(chosenArtists = favoriteArtists) }
            }
        }
    }

    fun searchArtists(value: String) {
        searchArtistsJob?.cancel()
        searchArtistsJob = viewModelScope.launch(Dispatchers.IO) {
            searchArtistsUseCase(
                searchBarValue = value,
                artistStateFlow = artistUiState
            )
        }
    }

    private fun handleFavoriteArtists() = viewModelScope.launch(Dispatchers.IO) {
        loadArtistsJob?.cancel()

        val user: User? = authorizedUser.firstOrNull()?.copy(
            favoriteArtists = artistUiState.value.chosenArtists,
            favoriteGenres = genreUiState.value.chosenGenres
        )

        updateUserUseCase(
            user = user,
            accessToken = userCreds.firstOrNull()?.accessToken
        )

        user?.let {
            uiStateDispatcher.setAuthorizedUser(user)
            uiStateDispatcher.sendUiEvent(
                UiEvent.Navigate(Route.Profile.getRouteWithNavArg(user.id.toString()))
            )
        }
    }

    private fun handleFavoriteGenres() = viewModelScope.launch(Dispatchers.IO) {
        loadArtists()
        uiStateDispatcher.sendUiEvent(UiEvent.Navigate(Route.EditFavoriteArtists))
    }

    fun setCurrentArtistPage(page: Int) = artistUiState.update {
        it.copy(currentPage = page)
    }

    fun setChosenGenres(genres: List<Genre>) = genreUiState.update {
        it.copy(chosenGenres = genres)
    }

    fun addChosenGenre(genre: Genre) = genreUiState.update {
        it.copy(chosenGenres = it.chosenGenres + genre)
    }

    fun setChosenArtists(artists: List<Artist>) = artistUiState.update {
        it.copy(chosenArtists = artists)
    }

    fun addChosenArtist(artist: Artist) = artistUiState.update {
        it.copy(chosenArtists = it.chosenArtists + artist)
    }
}