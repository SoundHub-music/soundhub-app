package com.soundhub.ui.pages.authentication.registration

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.soundhub.data.states.GenreUiState
import com.soundhub.ui.shared.layouts.music_preferences.ChooseGenresScreen

@Composable
fun PostRegisterChooseGenresScreen(registrationViewModel: RegistrationViewModel) {
	val genreUiState: GenreUiState by registrationViewModel
		.genreUiState
		.collectAsState()

	LaunchedEffect(key1 = genreUiState.chosenGenres) {
		Log.d("PostRegisterChooseGenresScreen", "chosen genres: ${genreUiState.chosenGenres}")
	}

	LaunchedEffect(key1 = genreUiState) {
		Log.d("PostRegisterChooseGenresScreen", "ui state: $genreUiState")
	}

	LaunchedEffect(key1 = true) {
		registrationViewModel.loadGenres()
	}

	ChooseGenresScreen(
		genreUiState = genreUiState,
		onItemPlateClick = registrationViewModel::onGenreItemClick,
		onNextButtonClick = registrationViewModel::onNextButtonClick
	)
}