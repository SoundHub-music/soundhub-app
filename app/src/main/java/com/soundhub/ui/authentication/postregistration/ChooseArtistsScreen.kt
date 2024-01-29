package com.soundhub.ui.authentication.postregistration

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.soundhub.R
import com.soundhub.ui.authentication.AuthenticationViewModel
import com.soundhub.ui.authentication.postregistration.components.ChooseMusicPreferencesPage

@Preview(showBackground = true)
@Composable
fun ChooseArtistsScreen(authViewModel: AuthenticationViewModel = hiltViewModel()) {
    val list = List(12) {"BTS"}
    ChooseMusicPreferencesPage(
        authViewModel = authViewModel,
        title = stringResource(id = R.string.choose_artists_page_title),
        itemsList = list
    )
}