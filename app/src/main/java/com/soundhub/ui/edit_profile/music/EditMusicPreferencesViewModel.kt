package com.soundhub.ui.edit_profile.music

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.soundhub.R
import com.soundhub.Route
import com.soundhub.data.dao.UserDao
import com.soundhub.data.model.Genre
import com.soundhub.data.model.User
import com.soundhub.domain.usecases.music.LoadArtistsUseCase
import com.soundhub.domain.usecases.music.LoadGenresUseCase
import com.soundhub.domain.usecases.music.SearchArtistsUseCase
import com.soundhub.domain.usecases.user.UpdateUserUseCase
import com.soundhub.ui.events.UiEvent
import com.soundhub.ui.music_preferences.BaseMusicPreferencesViewModel
import com.soundhub.ui.states.UiState
import com.soundhub.ui.viewmodels.UiStateDispatcher
import com.soundhub.utils.UiText
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class EditMusicPreferencesViewModel @Inject constructor(
    private val uiStateDispatcher: UiStateDispatcher,
    private val updateUserUseCase: UpdateUserUseCase,
    private val loadGenresUseCase: LoadGenresUseCase,
    private val userDao: UserDao,
    loadArtistsUseCase: LoadArtistsUseCase,
    searchArtistsUseCase: SearchArtistsUseCase
) : BaseMusicPreferencesViewModel(
    loadGenresUseCase,
    loadArtistsUseCase,
    searchArtistsUseCase
) {
    private val uiState: Flow<UiState> = uiStateDispatcher.uiState

    override fun onCleared() {
        super.onCleared()
        Log.d("EditMusicPreferencesViewModel", "viewmodel was cleared")
    }

    override fun loadGenres(): Job = viewModelScope.launch {
        val user: User? = userDao.getCurrentUser()
        val ( _, genres: List<Genre>, chosenGenres: List<Genre> ) = _genreUiState.value
        val favoriteGenres: List<Genre> = user?.favoriteGenres.orEmpty()

        if (chosenGenres.isEmpty())
            _genreUiState.update { it.copy(chosenGenres = favoriteGenres) }

        if (genres.isEmpty())
            loadGenresUseCase(genreUiState = _genreUiState)
        Log.d("EditMusicPreferencesViewModel", "loadGenres: ${genreUiState.value}")
    }

    override fun loadArtists(paginationUrl: String?) {
        super.loadArtists(paginationUrl)

        viewModelScope.launch {
            val user: User? = userDao.getCurrentUser()
            val favoriteArtists = user?.favoriteArtists.orEmpty()
            _artistUiState.update { it.copy(
                chosenArtists = favoriteArtists
            ) }
        }
    }

    override fun onNextButtonClick() = viewModelScope.launch(Dispatchers.Main) {
        val state: UiState? = uiState.firstOrNull()
        when (state?.currentRoute) {
            Route.EditFavoriteGenres.route -> onChooseGenresNextButtonClick()
            Route.EditFavoriteArtists.route -> onChooseArtistsNextButtonClick()
            else -> Unit
        }
    }

    override fun onChooseGenresNextButtonClick() = viewModelScope.launch(Dispatchers.Main) {
        if (_genreUiState.value.chosenGenres.isEmpty()) {
            val toastMessage = UiEvent.ShowToast(UiText.StringResource(R.string.choose_genres_warning))
            uiStateDispatcher.sendUiEvent(toastMessage)
            return@launch
        }

        uiStateDispatcher.sendUiEvent(UiEvent.Navigate(Route.EditFavoriteArtists))
    }

    override fun onChooseArtistsNextButtonClick() = viewModelScope.launch(Dispatchers.IO) {
        loadArtistsJob?.cancel()
        var toastMessage = UiText.StringResource(R.string.toast_update_music_preferences_successful)

        if (_artistUiState.value.chosenArtists.isEmpty()) {
            toastMessage = UiText.StringResource(R.string.choose_artist_warning)
            uiStateDispatcher.sendUiEvent(UiEvent.ShowToast(toastMessage))
            return@launch
        }
        val authorizedUser: User? = userDao.getCurrentUser()

        val user: User? = authorizedUser?.copy(
            favoriteArtists = artistUiState.value.chosenArtists,
            favoriteGenres = genreUiState.value.chosenGenres,
            favoriteArtistsIds = artistUiState.value.chosenArtists.map { it.id }
        )

        updateUserUseCase(user).onSuccess {
            withContext(Dispatchers.Main) {
                user?.let {
                    val route: Route = Route.Profile.getRouteWithNavArg(user.id.toString())
                    userDao.saveUser(user)
                    with(uiStateDispatcher) {
                        setAuthorizedUser(user)
                        sendUiEvent(UiEvent.ShowToast(toastMessage))
                        sendUiEvent(UiEvent.Navigate(route))
                    }
                }
            }
        }
        .onFailure {
            toastMessage = UiText.StringResource(R.string.toast_update_music_preferences_failed)
            uiStateDispatcher.sendUiEvent(UiEvent.ShowToast(toastMessage))
        }

    }
}
