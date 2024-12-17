package com.soundhub.presentation.shared.layouts.music_preferences

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.soundhub.R
import com.soundhub.domain.model.Genre
import com.soundhub.domain.states.GenreUiState
import com.soundhub.presentation.layout.PostRegisterGridLayout
import java.util.UUID

@Composable
fun ChooseGenresScreen(
	genreUiState: GenreUiState,
	onItemPlateClick: (isChosen: Boolean, genre: Genre) -> Unit,
	onNextButtonClick: () -> Unit
) {
	PostRegisterGridLayout<UUID>(
		items = genreUiState.genres,
		chosenItems = genreUiState.chosenGenres,
		isLoading = genreUiState.status.isLoading(),
		title = stringResource(id = R.string.screen_title_choose_genres),
		onItemPlateClick = { isChosen, item -> onItemPlateClick(isChosen, item as Genre) },
		onNextButtonClick = onNextButtonClick
	)
}