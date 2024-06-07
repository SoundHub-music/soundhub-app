package com.soundhub.ui.music_preferences

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.soundhub.R
import com.soundhub.data.enums.ApiStatus
import com.soundhub.data.model.Genre
import com.soundhub.ui.authentication.registration.components.MusicItemPlate
import com.soundhub.ui.authentication.registration.states.GenreUiState
import com.soundhub.ui.components.buttons.FloatingNextButton
import com.soundhub.ui.components.loaders.CircleLoader

@Composable
fun ChooseGenresScreen(
    genreState: GenreUiState,
    onItemPlateClick: (isChosen: Boolean, genre: Genre) -> Unit,
    onNextButtonClick: () -> Unit
) {
    val isLoading: Boolean = genreState.status == ApiStatus.LOADING

    Box(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.background)
            .fillMaxSize(),
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxSize()
        ) {
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 15.dp, top = 20.dp, end = 15.dp, bottom = 30.dp),
                text = stringResource(id = R.string.screen_title_choose_genres),
                fontSize = 32.sp,
                lineHeight = 42.sp,
                color = MaterialTheme.colorScheme.onSecondaryContainer,
                fontWeight = FontWeight.ExtraBold
            )

            if (isLoading) CircleLoader()
            else LazyVerticalGrid(
                columns = GridCells.Adaptive(minSize = 100.dp),
                contentPadding = PaddingValues(all = 10.dp),
                content = {
                    items(items = genreState.genres, key = { it.id }) {genre ->
                        MusicItemPlate(
                            modifier = Modifier.padding(bottom = 20.dp),
                            caption = genre.name ?: "",
                            thumbnailUrl = genre.pictureUrl,
                            onClick = { isChosen -> onItemPlateClick(isChosen, genre) },
                            isChosen = genre in genreState.chosenGenres,
                            width = 90.dp,
                            height = 90.dp
                        )
                    }
                }
            )
        }

        FloatingNextButton(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp),
            onClick = onNextButtonClick
        )
    }
}