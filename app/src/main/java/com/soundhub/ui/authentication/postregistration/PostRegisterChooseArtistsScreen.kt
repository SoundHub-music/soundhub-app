package com.soundhub.ui.authentication.postregistration

import android.widget.Toast
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import com.soundhub.R
import com.soundhub.ui.authentication.postregistration.states.ArtistUiState
import com.soundhub.ui.music_preferences.ChooseArtistsScreen
import com.soundhub.ui.viewmodels.UiStateDispatcher

@Composable
fun PostRegisterChooseArtistsScreen(
    registrationViewModel: RegistrationViewModel,
    uiStateDispatcher: UiStateDispatcher
) {
    val artistUiState: ArtistUiState by registrationViewModel.artistUiState.collectAsState()
    val context = LocalContext.current
    val lazyGridState = rememberLazyGridState()
    val toastWarningText = stringResource(id = R.string.choose_artist_warning)

    LaunchedEffect(key1 = lazyGridState.canScrollForward) {
        if (!lazyGridState.canScrollForward) {
            registrationViewModel.setCurrentArtistPage(artistUiState.currentPage + 1)
            registrationViewModel.loadArtists(artistUiState.currentPage)
        }
    }

    ChooseArtistsScreen(
        artistState = artistUiState,
        onItemPlateClick = { isChosen, artist ->
            if (isChosen) registrationViewModel.addChosenArtist(artist)
            else registrationViewModel.addChosenArtist(
                artistUiState.artists.filter { it.id != artist.id }
            )
        },
        onNextButtonClick = {
            if (artistUiState.chosenArtists.isNotEmpty())
                registrationViewModel
                    .onPostRegisterNextBtnClick()
            else Toast.makeText(
                context,
                toastWarningText,
                Toast.LENGTH_SHORT
            ).show()
        },
        onSearchFieldChange = { value ->
            uiStateDispatcher.updateSearchBarText(value)
            registrationViewModel.searchArtists(value)
        },
        uiStateDispatcher = uiStateDispatcher,
        lazyGridState = lazyGridState
    )
}