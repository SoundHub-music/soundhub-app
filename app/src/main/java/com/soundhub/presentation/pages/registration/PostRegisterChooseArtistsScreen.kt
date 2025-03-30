package com.soundhub.presentation.pages.registration

import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.paging.compose.collectAsLazyPagingItems
import com.soundhub.domain.states.ArtistUiState
import com.soundhub.presentation.shared.layouts.music_preferences.ChooseArtistsScreen
import com.soundhub.presentation.viewmodels.UiStateDispatcher

@Composable
fun PostRegisterChooseArtistsScreen(
	registrationViewModel: RegistrationViewModel,
	uiStateDispatcher: UiStateDispatcher
) {
	val artistUiState: ArtistUiState by registrationViewModel.artistUiState.collectAsState()
	val lazyGridState = rememberLazyGridState()

	val pagedArtists = remember {
		registrationViewModel.getArtistPage()
	}.collectAsLazyPagingItems()

	LaunchedEffect(key1 = true) {
		uiStateDispatcher.onSearchValueDebounceChange { pagedArtists.refresh() }
	}

	ChooseArtistsScreen(
		chosenArtists = artistUiState.chosenArtists,
		pagedArtists = pagedArtists,
		onItemPlateClick = registrationViewModel::onArtistItemClick,
		onNextButtonClick = registrationViewModel::onNextButtonClick,
		onSearchFieldChange = registrationViewModel::onSearchFieldChange,
		uiStateDispatcher = uiStateDispatcher,
		lazyGridState = lazyGridState
	)
}