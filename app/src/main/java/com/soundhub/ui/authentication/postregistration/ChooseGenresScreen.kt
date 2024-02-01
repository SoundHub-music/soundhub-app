package com.soundhub.ui.authentication.postregistration

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.soundhub.R
import com.soundhub.ui.authentication.AuthenticationViewModel
import com.soundhub.ui.authentication.postregistration.components.ChooseMusicPreferencesPage

@Composable
fun ChooseGenresScreen(
    authViewModel: AuthenticationViewModel = hiltViewModel(),
    navController: NavHostController
) {
    val list = List(12) {"Rap"}
    ChooseMusicPreferencesPage(
        authViewModel = authViewModel,
        title = stringResource(id = R.string.screen_title_choose_genres),
        itemsList = list,
        navController = navController
    )
}