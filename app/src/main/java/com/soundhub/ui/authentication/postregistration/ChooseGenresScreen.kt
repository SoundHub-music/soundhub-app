package com.soundhub.ui.authentication.postregistration

import android.util.Log
import android.widget.Toast
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.soundhub.R
import com.soundhub.data.model.Genre
import com.soundhub.ui.authentication.postregistration.components.MusicItemPlate
import com.soundhub.ui.components.CircleLoader
import com.soundhub.ui.components.buttons.FloatingNextButton
import com.soundhub.Route

@Composable
fun ChooseGenresScreen(
    registrationViewModel: RegistrationViewModel = hiltViewModel()
) {
    val genres: List<Genre> = registrationViewModel.genres.collectAsState().value
    val chosenGenres: List<Genre> = registrationViewModel.chosenGenres.collectAsState().value
    val isLoading: Boolean = registrationViewModel.isLoading.collectAsState().value

    val context = LocalContext.current
    val toastWarningText: String = stringResource(id = R.string.choose_genres_warning)

    LaunchedEffect(key1 = chosenGenres) {
        Log.d("ChooseGenresScreen", "chosen genres: $chosenGenres")
    }

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
                    itemsIndexed(genres) { _, genre ->
                        MusicItemPlate(
                            modifier = Modifier.padding(bottom = 20.dp),
                            caption = genre.name ?: "",
                            thumbnailUrl = genre.pictureURL,
                            onClick = { isChosen ->
                                if (isChosen) {
                                    registrationViewModel.setChosenGenres(genre)
                                } else registrationViewModel.setChosenGenres(
                                    chosenGenres.filter { it.id != genre.id }
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
                .padding(16.dp)
        ) {
            if (chosenGenres.isNotEmpty())
                registrationViewModel
                    .onPostRegisterNextBtnClick(Route.Authentication.ChooseGenres)
            else Toast.makeText(
                context,
                toastWarningText,
                Toast.LENGTH_SHORT
            ).show()
        }
    }
}