package com.soundhub.ui.authentication.postregistration

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.soundhub.R
import com.soundhub.data.model.Artist
import com.soundhub.ui.authentication.postregistration.components.MusicItemPlate
import com.soundhub.ui.components.buttons.FloatingNextButton
import com.soundhub.Route

@Composable
fun ChooseArtistsScreen(
    registrationViewModel: RegistrationViewModel = hiltViewModel()
) {
    val artists = emptyList<Artist>()

    Box(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.background)
            .fillMaxSize()
    ) {
        Column {
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

            LazyVerticalGrid(
                columns = GridCells.Adaptive(minSize = 100.dp),
                contentPadding = PaddingValues(all = 10.dp),
                content = {
                    itemsIndexed(artists) { _, artist ->
                        MusicItemPlate(
                            modifier = Modifier.padding(bottom = 20.dp),
                            caption = artist.name,
                            thumbnailUrl = artist.thumbnailUrl,
                            onClick = { isChosen ->
                                if (isChosen)
                                    registrationViewModel.setChosenArtists(artist)
                                else registrationViewModel.setChosenArtists(
                                     artists.filter { it.id != artist.id }.toMutableList()
                                )
                            }
                        )
                    }
                }
            )
        }

        FloatingNextButton(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp),
            onClick = { registrationViewModel
                .onPostRegisterNextBtnClick(Route.Authentication.ChooseArtists) }
        )
    }
}