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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.soundhub.R
import com.soundhub.data.model.Artist
import com.soundhub.ui.authentication.AuthenticationViewModel
import com.soundhub.ui.authentication.postregistration.components.ItemPlate
import com.soundhub.ui.components.FloatingNextButton
import com.soundhub.utils.Route

@Composable
fun ChooseArtistsScreen(
    authViewModel: AuthenticationViewModel = hiltViewModel(),
    navController: NavHostController
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
                style = TextStyle(
                    fontSize = 32.sp,
                    fontFamily = FontFamily(Font(R.font.nunito_extrabold)),
                    color = MaterialTheme.colorScheme.onSecondaryContainer,
                )
            )

            LazyVerticalGrid(
                columns = GridCells.Adaptive(minSize = 100.dp),
                contentPadding = PaddingValues(all = 10.dp),
                content = {
                    itemsIndexed(artists) { _, artist ->
                        ItemPlate(
                            modifier = Modifier.padding(bottom = 20.dp),
                            caption = artist.name,
                            icon = painterResource(id = R.drawable.musical_note),
                            onClick = { isChosen ->
                                if (isChosen)
                                    authViewModel.setChosenArtists(artist)
                                else authViewModel.setChosenArtists(
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
            onClick = { authViewModel.onPostRegisterNextButtonClick(Route.Authentication.ChooseArtists) }
        )
    }
}