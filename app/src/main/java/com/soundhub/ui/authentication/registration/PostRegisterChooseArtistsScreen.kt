package com.soundhub.ui.authentication.registration

import android.util.Log
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.soundhub.ui.authentication.registration.states.ArtistUiState
import com.soundhub.ui.music_preferences.ChooseArtistsScreen
import com.soundhub.ui.viewmodels.UiStateDispatcher

@Composable
fun PostRegisterChooseArtistsScreen(
    registrationViewModel: RegistrationViewModel,
    uiStateDispatcher: UiStateDispatcher
) {
    val artistUiState: ArtistUiState by registrationViewModel.artistUiState.collectAsState()
    val lazyGridState = rememberLazyGridState()

    LaunchedEffect(key1 = lazyGridState.canScrollForward) {
        if (!lazyGridState.canScrollForward) {
            registrationViewModel.loadArtists(artistUiState.pagination?.urls?.next)
        }
    }

    LaunchedEffect(key1 = true) {
        registrationViewModel.loadArtists()
    }

    LaunchedEffect(key1 = artistUiState) {
        Log.d("PostRegisterChooseArtistsScreen", "ui state: $artistUiState")
    }

    ChooseArtistsScreen(
        artistUiState = artistUiState,
        onItemPlateClick = registrationViewModel::onArtistItemClick,
        onNextButtonClick = registrationViewModel::onNextButtonClick,
        onSearchFieldChange = registrationViewModel::onSearchFieldChange,
        uiStateDispatcher = uiStateDispatcher,
        lazyGridState = lazyGridState
    )
}