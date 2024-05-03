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
import com.soundhub.domain.usecases.user.UpdateUserUseCase
import com.soundhub.ui.authentication.postregistration.states.ArtistUiState
import com.soundhub.ui.authentication.postregistration.states.GenreUiState
import com.soundhub.ui.events.UiEvent
import com.soundhub.ui.states.UiState
import com.soundhub.ui.viewmodels.UiStateDispatcher
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditMusicPreferencesViewModel @Inject constructor(
    private val uiStateDispatcher: UiStateDispatcher,
    private val updateUserUseCase: UpdateUserUseCase,
    private val loadGenresUseCase: LoadGenresUseCase,
    private val loadArtistsUseCase: LoadArtistsUseCase,
    userCredsStore: UserCredsStore
): ViewModel() {
    val genreUiState: MutableStateFlow<GenreUiState> = MutableStateFlow(GenreUiState())
    val artistUiState: MutableStateFlow<ArtistUiState> = MutableStateFlow(ArtistUiState())

    private val uiState: StateFlow<UiState> = uiStateDispatcher.uiState.asStateFlow()
    private val currentUserState: MutableStateFlow<User?> = MutableStateFlow(null)
    private val userCreds: Flow<UserPreferences> = userCredsStore.getCreds()

    init {
        viewModelScope.launch {
            uiState.collect { state ->
                currentUserState.update { state.authorizedUser }
                loadGenres()
            }
        }
    }

    fun onNextButtonClick() = viewModelScope.launch {
        val state: UiState? = uiState.firstOrNull()
        when (state?.currentRoute) {
            Route.EditFavoriteGenres.route -> handleFavoriteGenres()
            Route.EditFavoriteArtists.route -> handleFavoriteArtists()
            else -> Unit
        }
    }

    private fun loadGenres() = viewModelScope.launch(Dispatchers.IO) {
        val favoriteGenres: List<Genre> = currentUserState.value
            ?.favoriteGenres ?: emptyList()

        loadGenresUseCase(genreUiState = genreUiState)
        genreUiState.update { it.copy(chosenGenres = favoriteGenres) }
        Log.d("EditMusicPreferencesViewModel", "loadGenre: $genreUiState")
    }

    private fun loadArtist() = viewModelScope.launch(Dispatchers.IO) {
        val favoriteArtists: List<Artist> = currentUserState.value
            ?.favoriteArtists ?: emptyList()

        loadArtistsUseCase(
            artistUiState = artistUiState,
            genreUiState = genreUiState
        )
        artistUiState.update { it.copy(chosenArtists = favoriteArtists) }
    }

    private fun handleFavoriteArtists() = viewModelScope.launch(Dispatchers.IO) {
        currentUserState.update {
            it?.copy(
                favoriteArtists = artistUiState.value.chosenArtists,
                favoriteGenres = genreUiState.value.chosenGenres
            )
        }

        updateUserUseCase(
            user = currentUserState.value,
            accessToken = userCreds.firstOrNull()?.accessToken
        )

        currentUserState.value?.let {
            uiStateDispatcher.setAuthorizedUser(it)
            uiStateDispatcher.sendUiEvent(
                UiEvent.Navigate(Route.Profile.getRouteWithNavArg(it.id.toString()))
            )
        }
    }

    private fun handleFavoriteGenres() = viewModelScope.launch(Dispatchers.IO) {
        loadArtist()
        uiStateDispatcher.sendUiEvent(UiEvent.Navigate(Route.EditFavoriteArtists))
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