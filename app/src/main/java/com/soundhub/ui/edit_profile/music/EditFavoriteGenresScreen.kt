package com.soundhub.ui.edit_profile.music

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.soundhub.data.model.Genre
import com.soundhub.ui.authentication.registration.states.GenreUiState
import com.soundhub.ui.music_preferences.ChooseGenresScreen

@Composable
fun EditFavoriteGenresScreen(
    editMusicPrefViewModel: EditMusicPreferencesViewModel
) {
    val genreUiState: GenreUiState by editMusicPrefViewModel
        .genreUiState
        .collectAsState()

    val chosenGenres: List<Genre> = genreUiState.chosenGenres

    LaunchedEffect(key1 = chosenGenres) {
        Log.d("EditFavoriteGenresScreen", "chosen genres: $chosenGenres")
    }

    ChooseGenresScreen(
        genreUiState = genreUiState,
        onItemPlateClick = { isChosen, genre ->
           if (isChosen)
               editMusicPrefViewModel.addChosenGenre(genre)
           else editMusicPrefViewModel.setChosenGenres(
               chosenGenres.filter { it.id != genre.id }
           )
        },
        onNextButtonClick = editMusicPrefViewModel::onNextButtonClick
    )
}