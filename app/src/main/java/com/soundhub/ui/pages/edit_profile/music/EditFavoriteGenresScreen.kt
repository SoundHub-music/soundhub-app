package com.soundhub.ui.pages.edit_profile.music

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.soundhub.data.model.Genre
import com.soundhub.data.states.GenreUiState
import com.soundhub.ui.shared.layouts.music_preferences.ChooseGenresScreen

@Composable
fun EditFavoriteGenresScreen(editMusicPrefViewModel: EditMusicPreferencesViewModel) {
    val genreUiState: GenreUiState by editMusicPrefViewModel
        .genreUiState
        .collectAsState()

    val chosenGenres: List<Genre> = genreUiState.chosenGenres

    LaunchedEffect(key1 = chosenGenres) {
        Log.d("EditFavoriteGenresScreen", "chosen genres: $chosenGenres")
    }

    LaunchedEffect(key1 = true) {
        editMusicPrefViewModel.loadGenres()
    }

    ChooseGenresScreen(
        genreUiState = genreUiState,
        onItemPlateClick = editMusicPrefViewModel::onGenreItemClick,
        onNextButtonClick = editMusicPrefViewModel::onNextButtonClick
    )
}