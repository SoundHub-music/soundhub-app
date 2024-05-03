package com.soundhub.ui.authentication.postregistration

import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import com.soundhub.R
import com.soundhub.ui.authentication.postregistration.states.GenreUiState
import com.soundhub.ui.music_preferences.ChooseGenresScreen

@Composable
fun PostRegisterChooseGenresScreen(
    registrationViewModel: RegistrationViewModel 
) {
    val genreState: GenreUiState by registrationViewModel
        .genreUiState
        .collectAsState()

    val context = LocalContext.current
    val toastWarningText: String = stringResource(id = R.string.choose_genres_warning)

    LaunchedEffect(key1 = genreState.chosenGenres) {
        Log.d("ChooseGenresScreen", "chosen genres: ${genreState.chosenGenres}")
    }

    ChooseGenresScreen(
        genreState = genreState,
        onItemPlateClick = { isChosen, genre ->
            if (isChosen)
                registrationViewModel.addChosenGenre(genre)
            else registrationViewModel.addChosenGenre(
                genreState.chosenGenres.filter { it.id != genre.id }
            )
        },
        onNextButtonClick = {
            if (genreState.chosenGenres.isNotEmpty())
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