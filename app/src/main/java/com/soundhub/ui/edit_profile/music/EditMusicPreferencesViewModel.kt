package com.soundhub.ui.edit_profile.music

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.soundhub.Route
import com.soundhub.data.model.Artist
import com.soundhub.data.model.Genre
import com.soundhub.data.model.User
import com.soundhub.domain.usecases.music.LoadArtistsUseCase
import com.soundhub.domain.usecases.music.LoadGenresUseCase
import com.soundhub.domain.usecases.music.SearchArtistsUseCase
import com.soundhub.domain.usecases.user.UpdateUserUseCase
import com.soundhub.ui.authentication.registration.states.ArtistUiState
import com.soundhub.ui.authentication.registration.states.GenreUiState
import com.soundhub.ui.events.UiEvent
import com.soundhub.ui.states.UiState
import com.soundhub.ui.viewmodels.UiStateDispatcher
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
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
): ViewModel() {
    private val _genreUiState: MutableStateFlow<GenreUiState> = MutableStateFlow(GenreUiState())
    val genreUiState = _genreUiState.asStateFlow()

    private val _artistUiState: MutableStateFlow<ArtistUiState> = MutableStateFlow(ArtistUiState())
    val artistUiState = _artistUiState.asStateFlow()

    private val uiState: Flow<UiState> = uiStateDispatcher.uiState
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
            val ( _, genres: List<Genre>, chosenGenres: List<Genre> ) = _genreUiState.value
            val favoriteGenres: List<Genre> = user
                ?.favoriteGenres.orEmpty()

            if (chosenGenres.isEmpty())
                _genreUiState.update { it.copy(chosenGenres = favoriteGenres) }

            if (genres.isEmpty())
                loadGenresUseCase(genreUiState = _genreUiState)
            Log.d("EditMusicPreferencesViewModel", "loadGenres: ${genreUiState.value}")
        }
    }

    fun loadArtists(page: Int = 1) {
        loadArtistsJob = viewModelScope.launch(Dispatchers.IO) {
            authorizedUser.collect { user ->
                val favoriteArtists: List<Artist> = user
                    ?.favoriteArtists.orEmpty()

                loadArtistsUseCase(
                    artistUiState = _artistUiState,
                    genreUiState = _genreUiState,
                    page = page
                )
                _artistUiState.update { it.copy(chosenArtists = favoriteArtists) }
            }
        }
    }

    fun searchArtists(value: String) {
        searchArtistsJob?.cancel()
        searchArtistsJob = viewModelScope.launch(Dispatchers.IO) {
            searchArtistsUseCase(
                searchBarValue = value,
                artistStateFlow = _artistUiState
            )
        }
    }

    private fun handleFavoriteArtists() = viewModelScope.launch(Dispatchers.IO) {
        loadArtistsJob?.cancel()

        val user: User? = authorizedUser.firstOrNull()?.copy(
            favoriteArtists = artistUiState.value.chosenArtists,
            favoriteGenres = genreUiState.value.chosenGenres
        )

        updateUserUseCase(user)

        user?.let {
            val route: Route = Route.Profile.getRouteWithNavArg(user.id.toString())
            with(uiStateDispatcher) {
                setAuthorizedUser(user)
                sendUiEvent(
                    UiEvent.Navigate(route)
                )
            }
        }
    }

    private fun handleFavoriteGenres() = viewModelScope.launch(Dispatchers.IO) {
        loadArtists()
        uiStateDispatcher.sendUiEvent(UiEvent.Navigate(Route.EditFavoriteArtists))
    }

    fun setCurrentArtistPage(page: Int) = _artistUiState.update {
        it.copy(currentPage = page)
    }

    fun setChosenGenres(genres: List<Genre>) = _genreUiState.update {
        it.copy(chosenGenres = genres)
    }

    fun addChosenGenre(genre: Genre) = _genreUiState.update {
        it.copy(chosenGenres = it.chosenGenres + genre)
    }

    fun setChosenArtists(artists: List<Artist>) = _artistUiState.update {
        it.copy(chosenArtists = artists)
    }

    fun addChosenArtist(artist: Artist) = _artistUiState.update {
        it.copy(chosenArtists = it.chosenArtists + artist)
    }
}