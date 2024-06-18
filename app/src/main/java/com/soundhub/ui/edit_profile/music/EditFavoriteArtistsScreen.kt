package com.soundhub.ui.edit_profile.music

import android.util.Log
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.soundhub.data.model.Artist
import com.soundhub.ui.authentication.registration.states.ArtistUiState
import com.soundhub.ui.music_preferences.ChooseArtistsScreen
import com.soundhub.ui.viewmodels.UiStateDispatcher

@Composable
fun EditFavoriteArtistsScreen(
    editMusicPrefViewModel: EditMusicPreferencesViewModel,
    uiStateDispatcher: UiStateDispatcher
) {
    val artistUiState: ArtistUiState by editMusicPrefViewModel
        .artistUiState.collectAsState()
    val lazyGridState = rememberLazyGridState()
    val chosenArtists: List<Artist> = artistUiState.chosenArtists

    LaunchedEffect(key1 = artistUiState) {
        Log.d("EditFavoriteArtistsScreen", "artist state: $artistUiState")
    }

    LaunchedEffect(key1 = lazyGridState.canScrollForward) {
        if (!lazyGridState.canScrollForward) {
            editMusicPrefViewModel.setCurrentArtistPage(artistUiState.currentPage + 1)
            editMusicPrefViewModel.loadArtists(artistUiState.currentPage)
        }
    }

    ChooseArtistsScreen(
        artistUiState = artistUiState,
        onItemPlateClick = { isChosen, artist ->
            if (isChosen)
                editMusicPrefViewModel.addChosenArtist(artist)
            else editMusicPrefViewModel.setChosenArtists(
                chosenArtists.filter { it.id != artist.id }
            )
        },
        onNextButtonClick = editMusicPrefViewModel::onNextButtonClick,
        onSearchFieldChange = { value ->
            uiStateDispatcher.updateSearchBarText(value)
            editMusicPrefViewModel.searchArtists(value)
        },
        uiStateDispatcher = uiStateDispatcher,
        lazyGridState = lazyGridState
    )
}