package com.soundhub.ui.edit_profile.music

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.soundhub.data.model.Artist
import com.soundhub.ui.authentication.postregistration.states.ArtistUiState
import com.soundhub.ui.music_preferences.ChooseArtistsScreen
import com.soundhub.ui.viewmodels.UiStateDispatcher

@Composable
fun EditFavoriteArtistsScreen(
    editMusicPrefViewModel: EditMusicPreferencesViewModel,
    uiStateDispatcher: UiStateDispatcher
) {
    val artistsUiState: ArtistUiState by editMusicPrefViewModel
        .artistUiState.collectAsState()
    val chosenArtists: List<Artist> = artistsUiState.chosenArtists

    LaunchedEffect(key1 = artistsUiState) {
        Log.d("EditFavoriteArtistsScreen", "artist state: $artistsUiState")
    }

    ChooseArtistsScreen(
        artistState = artistsUiState,
        onItemPlateClick = { isChosen, artist ->
            if (isChosen)
                editMusicPrefViewModel.addChosenArtist(artist)
            else editMusicPrefViewModel.setChosenArtists(
                chosenArtists.filter { it.id != artist.id }
            )
        },
        onNextButtonClick = editMusicPrefViewModel::onNextButtonClick,
        uiStateDispatcher = uiStateDispatcher
    )
}