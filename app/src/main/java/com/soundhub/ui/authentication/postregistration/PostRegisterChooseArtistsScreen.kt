package com.soundhub.ui.authentication.postregistration

import android.widget.Toast
import androidx.compose.runtime.Composable
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
    val artistState: ArtistUiState by registrationViewModel.artistUiState.collectAsState()
    val context = LocalContext.current
    val toastWarningText = stringResource(id = R.string.choose_artist_warning)

    ChooseArtistsScreen(
        artistState = artistState,
        onItemPlateClick = { isChosen, artist ->
            if (isChosen) registrationViewModel.addChosenArtist(artist)
            else registrationViewModel.addChosenArtist(
                artistState.artists.filter { it.id != artist.id }
            )
        },
        onNextButtonClick = {
            if (artistState.chosenArtists.isNotEmpty())
                registrationViewModel
                    .onPostRegisterNextBtnClick()
            else Toast.makeText(
                context,
                toastWarningText,
                Toast.LENGTH_SHORT
            ).show()
        },
        uiStateDispatcher = uiStateDispatcher
    )
}