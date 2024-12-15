package com.soundhub.ui.shared.layouts.music_preferences

import android.content.res.Configuration
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.soundhub.R
import com.soundhub.data.datastore.UserSettingsStore
import com.soundhub.data.states.ArtistUiState
import com.soundhub.data.states.UiState
import com.soundhub.domain.model.Artist
import com.soundhub.ui.layout.PostRegisterGridLayout
import com.soundhub.ui.viewmodels.UiStateDispatcher

@Composable
fun ChooseArtistsScreen(
	artistUiState: ArtistUiState,
	onItemPlateClick: (isChosen: Boolean, artist: Artist) -> Unit,
	uiStateDispatcher: UiStateDispatcher,
	onNextButtonClick: () -> Unit,
	onSearchFieldChange: (value: String) -> Unit,
	lazyGridState: LazyGridState,
) {
	val uiState by uiStateDispatcher.uiState.collectAsState(initial = UiState())
	val artists = remember(artistUiState.artists) { artistUiState.artists }

	PostRegisterGridLayout<Int>(
		items = artists,
		chosenItems = artistUiState.chosenArtists,
		title = stringResource(id = R.string.screen_title_choose_artists),
		onItemPlateClick = { isChosen, item -> onItemPlateClick(isChosen, item as Artist) },
		onNextButtonClick = onNextButtonClick,
		lazyGridState = lazyGridState,
		isLoading = artistUiState.status.isLoading(),
		topContent = {
			OutlinedTextField(
				value = uiState.searchBarText,
				onValueChange = onSearchFieldChange,
				colors = TextFieldDefaults.colors(
					unfocusedContainerColor = MaterialTheme.colorScheme.surfaceContainer
				),
				singleLine = true,
				trailingIcon = {
					Icon(imageVector = Icons.Rounded.Search, contentDescription = "search bar")
				},
				modifier = Modifier
					.fillMaxWidth()
					.padding(bottom = 5.dp),
			)
		}
	)
}

@Composable
@Preview(
	showSystemUi = true,
	uiMode = Configuration.UI_MODE_NIGHT_YES
			or Configuration.UI_MODE_TYPE_NORMAL
)
private fun ChooseArtistsPreview() {
	val context = LocalContext.current
	ChooseArtistsScreen(
		onItemPlateClick = { _, _ -> },
		artistUiState = ArtistUiState(),
		uiStateDispatcher = UiStateDispatcher(UserSettingsStore(context)),
		onNextButtonClick = {},
		onSearchFieldChange = {},
		lazyGridState = rememberLazyGridState()
	)
}