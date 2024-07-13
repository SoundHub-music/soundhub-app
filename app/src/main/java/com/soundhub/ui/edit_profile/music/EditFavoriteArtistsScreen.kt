package com.soundhub.ui.edit_profile.music

import android.util.Log
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.soundhub.data.states.ArtistUiState
import com.soundhub.ui.components.layouts.music_preferences.ChooseArtistsScreen
import com.soundhub.ui.viewmodels.UiStateDispatcher

@Composable
fun EditFavoriteArtistsScreen(
    editMusicPrefViewModel: EditMusicPreferencesViewModel,
    uiStateDispatcher: UiStateDispatcher
) {
    val artistUiState: ArtistUiState by editMusicPrefViewModel
        .artistUiState.collectAsState()
    val lazyGridState = rememberLazyGridState()

    LaunchedEffect(key1 = artistUiState) {
        Log.d("EditFavoriteArtistsScreen", "artist state: $artistUiState")
    }

    LaunchedEffect(key1 = lazyGridState.canScrollForward) {
        if (!lazyGridState.canScrollForward) {
            editMusicPrefViewModel.loadArtists(artistUiState.pagination?.urls?.next)
        }
    }

    LaunchedEffect(key1 = true) {
        editMusicPrefViewModel.loadArtists()
    }

    ChooseArtistsScreen(
        artistUiState = artistUiState,
        onItemPlateClick = editMusicPrefViewModel::onArtistItemClick,
        onNextButtonClick = editMusicPrefViewModel::onNextButtonClick,
        onSearchFieldChange = editMusicPrefViewModel::onSearchFieldChange,
        uiStateDispatcher = uiStateDispatcher,
        lazyGridState = lazyGridState
    )
}