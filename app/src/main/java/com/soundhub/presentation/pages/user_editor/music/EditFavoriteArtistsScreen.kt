package com.soundhub.presentation.pages.user_editor.music

import android.util.Log
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.soundhub.domain.states.ArtistUiState
import com.soundhub.presentation.shared.layouts.music_preferences.ChooseArtistsScreen
import com.soundhub.presentation.viewmodels.UiStateDispatcher

@Composable
fun EditFavoriteArtistsScreen(
	editMusicPrefViewModel: EditMusicPreferencesViewModel,
	uiStateDispatcher: UiStateDispatcher
) {
	val artistUiState: ArtistUiState by editMusicPrefViewModel
		.artistUiState.collectAsState()
	val lazyGridState = rememberLazyGridState()

	LaunchedEffect(key1 = artistUiState) {
		Log.d("EditFavoriteArtistsScreen", "chosen artists: ${artistUiState.chosenArtists}")
	}

	LaunchedEffect(key1 = lazyGridState.canScrollForward) {
		if (!lazyGridState.canScrollForward) {
			editMusicPrefViewModel.loadArtists(artistUiState.pagination?.urls?.next)
		}
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