package com.soundhub.presentation.pages.user_editor.music

import android.util.Log
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import com.soundhub.domain.states.ArtistUiState
import com.soundhub.domain.states.UiState
import com.soundhub.presentation.shared.layouts.music_preferences.ChooseArtistsScreen
import com.soundhub.presentation.viewmodels.UiStateDispatcher
import kotlinx.coroutines.FlowPreview

@OptIn(FlowPreview::class)
@Composable
fun EditFavoriteArtistsScreen(
	editMusicPrefViewModel: EditMusicPreferencesViewModel,
	uiStateDispatcher: UiStateDispatcher
) {
	val artistUiState: ArtistUiState by editMusicPrefViewModel
		.artistUiState.collectAsState()


	val uiState by uiStateDispatcher.uiState.collectAsState(initial = UiState())

	val lazyGridState = rememberLazyGridState()

	val pagedArtists = remember { editMusicPrefViewModel.getArtistPage() }
		.collectAsLazyPagingItems()

	val loadState = pagedArtists.loadState

	val isLoading = loadState.refresh is LoadState.Loading || loadState.append is LoadState.Loading

	LaunchedEffect(uiState.searchBarText) {
		uiStateDispatcher.onSearchValueDebounceChange {
			pagedArtists.refresh()
		}
	}

	LaunchedEffect(key1 = artistUiState) {
		Log.d("EditFavoriteArtistsScreen", "chosen artists: ${artistUiState.chosenArtists}")
	}

	LaunchedEffect(pagedArtists.loadState) {
		Log.d("EditFavoriteArtistsScreen", "load state: ${pagedArtists.loadState}")
	}

	ChooseArtistsScreen(
		artists = artistUiState.artists,
		pagedArtists = pagedArtists,
		chosenArtists = artistUiState.chosenArtists,
		isLoading = isLoading,
		onItemPlateClick = editMusicPrefViewModel::onArtistItemClick,
		onNextButtonClick = editMusicPrefViewModel::onNextButtonClick,
		onSearchFieldChange = editMusicPrefViewModel::onSearchFieldChange,
		uiStateDispatcher = uiStateDispatcher,
		lazyGridState = lazyGridState
	)
}