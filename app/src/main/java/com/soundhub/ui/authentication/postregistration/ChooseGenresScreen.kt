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
import com.soundhub.data.model.Genre
import com.soundhub.ui.authentication.AuthenticationViewModel
import com.soundhub.ui.authentication.postregistration.components.ItemPlate
import com.soundhub.ui.components.CircleLoader
import com.soundhub.ui.components.buttons.FloatingNextButton
import com.soundhub.utils.Route

@Composable
fun ChooseGenresScreen(
    authViewModel: AuthenticationViewModel = hiltViewModel(),
    navController: NavHostController
) {
    val postRegistrationViewModel: PostRegistrationViewModel = hiltViewModel()
    val genres: List<Genre> = postRegistrationViewModel.genres.collectAsState().value
    val chosenGenres: List<Genre> = postRegistrationViewModel.chosenGenres.collectAsState().value
    val isLoading: Boolean = postRegistrationViewModel.isLoading.collectAsState().value

    val context = LocalContext.current
    val floatingBtnWarningText: String = stringResource(id = R.string.choose_genres_warning)

    LaunchedEffect(key1 = chosenGenres) {
        Log.d("chosen_genres", chosenGenres.toString())
    }

    Box(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.background)
            .fillMaxSize()
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 15.dp, top = 20.dp, end = 15.dp, bottom = 30.dp),
                text = stringResource(id = R.string.screen_title_choose_genres),
                style = TextStyle(
                    fontSize = 32.sp,
                    fontFamily = FontFamily(Font(R.font.nunito_extrabold)),
                    color = MaterialTheme.colorScheme.onSecondaryContainer,
                )
            )

            if (isLoading) CircleLoader()
            else LazyVerticalGrid(
                columns = GridCells.Adaptive(minSize = 100.dp),
                contentPadding = PaddingValues(all = 10.dp),
                content = {
                    itemsIndexed(genres) { _, plate ->
                        ItemPlate(
                            modifier = Modifier.padding(bottom = 20.dp),
                            caption = plate.name ?: "",
                            icon = painterResource(id = R.drawable.musical_note),
                            onClick = { isChosen ->
                                if (isChosen) {
                                    postRegistrationViewModel.setChosenGenres(plate)
                                } else postRegistrationViewModel.setChosenGenres(
                                    chosenGenres.filter { it.id != plate.id }
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
            onClick = {
                if (chosenGenres.isNotEmpty())
                    authViewModel.onPostRegisterNextButtonClick(Route.Authentication.ChooseGenres)
                else Toast.makeText(
                    context,
                    floatingBtnWarningText,
                    Toast.LENGTH_SHORT
                ).show()
            }
        )
    }
}