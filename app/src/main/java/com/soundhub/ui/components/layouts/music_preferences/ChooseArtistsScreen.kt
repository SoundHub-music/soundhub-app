package com.soundhub.ui.components.layouts.music_preferences

import android.content.res.Configuration
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.soundhub.R
import com.soundhub.data.datastore.UserSettingsStore
import com.soundhub.data.enums.ApiStatus
import com.soundhub.data.model.Artist
import com.soundhub.data.states.ArtistUiState
import com.soundhub.ui.components.buttons.FloatingNextButton
import com.soundhub.ui.components.containers.ContentContainer
import com.soundhub.data.states.UiState
import com.soundhub.ui.components.layouts.music_preferences.components.MusicItemPlate
import com.soundhub.ui.viewmodels.UiStateDispatcher

@Composable
fun ChooseArtistsScreen(
    artistUiState: ArtistUiState,
    onItemPlateClick: (isChosen: Boolean, artist: Artist) -> Unit,
    uiStateDispatcher: UiStateDispatcher,
    onNextButtonClick: () -> Unit,
    onSearchFieldChange: (value: String) -> Unit,
    lazyGridState: LazyGridState
) {
    val uiState by uiStateDispatcher.uiState.collectAsState(initial = UiState())
    val artists = remember(artistUiState.artists) { artistUiState.artists }
    val chosenArtistIds = remember(artistUiState.chosenArtists) {
        artistUiState.chosenArtists.map { it.id }
    }

    LaunchedEffect(key1 = artistUiState) {
        Log.d("ChooseArtistsScreen", "ui state: $artistUiState")
    }

    ContentContainer(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.background)
            .fillMaxSize(),
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 15.dp, top = 20.dp, end = 15.dp, bottom = 30.dp),
                text = stringResource(id = R.string.screen_title_choose_artists),
                fontSize = 32.sp,
                lineHeight = 42.sp,
                color = MaterialTheme.colorScheme.onSecondaryContainer,
                fontWeight = FontWeight.ExtraBold
            )

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

            LazyVerticalGrid(
                columns = GridCells.Adaptive(minSize = 100.dp),
                contentPadding = PaddingValues(all = 10.dp),
                state = lazyGridState
            ) {
                items(items = artists) { artist ->
                    MusicItemPlate(
                        modifier = Modifier.padding(bottom = 20.dp),
                        caption = artist.name ?: "",
                        thumbnailUrl = artist.cover,
                        onClick = { isChosen -> onItemPlateClick(isChosen, artist) },
                        isChosen = artist.id in chosenArtistIds,
                        width = 90.dp,
                        height = 90.dp
                    )
                }
            }
        }

        FloatingNextButton(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp),
            onClick = onNextButtonClick,
            isLoading = artistUiState.status != ApiStatus.SUCCESS
        )
    }
}

@Composable
@Preview(showSystemUi = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES
            or Configuration.UI_MODE_TYPE_NORMAL
)
private fun ChooseArtistsPreview() {
    val context = LocalContext.current
    ChooseArtistsScreen(
        artistUiState = ArtistUiState(),
        onItemPlateClick = {_, _ ->  },
        uiStateDispatcher = UiStateDispatcher(UserSettingsStore(context)),
        onNextButtonClick = {},
        onSearchFieldChange = {},
        lazyGridState = rememberLazyGridState()
    )
}