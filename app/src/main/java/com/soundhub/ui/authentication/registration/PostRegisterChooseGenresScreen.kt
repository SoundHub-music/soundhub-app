package com.soundhub.ui.authentication.registration

import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import com.soundhub.R
import com.soundhub.ui.authentication.registration.states.GenreUiState
import com.soundhub.ui.music_preferences.ChooseGenresScreen

@Composable
fun PostRegisterChooseGenresScreen(
    registrationViewModel: RegistrationViewModel 
) {
    val genreUiState: GenreUiState by registrationViewModel
        .genreUiState
        .collectAsState()

    val context = LocalContext.current
    val toastWarningText: String = stringResource(id = R.string.choose_genres_warning)

    LaunchedEffect(key1 = genreUiState.chosenGenres) {
        Log.d("PostRegisterChooseGenresScreen", "chosen genres: ${genreUiState.chosenGenres}")
    }

    LaunchedEffect(key1 = genreUiState) {
        Log.d("PostRegisterChooseGenresScreen", "ui state: $genreUiState")
    }

    ChooseGenresScreen(
        genreUiState = genreUiState,
        onItemPlateClick = { isChosen, genre ->
            if (isChosen)
                registrationViewModel.addChosenGenre(genre)
            else registrationViewModel.addChosenGenre(
                genreUiState.chosenGenres.filter { it.id != genre.id }
            )
        },
        onNextButtonClick = {
            if (genreUiState.chosenGenres.isNotEmpty())
                registrationViewModel
                    .onPostRegisterNextBtnClick()
            else Toast.makeText(
                context,
                toastWarningText,
                Toast.LENGTH_SHORT
            ).show()
        }
    )
}