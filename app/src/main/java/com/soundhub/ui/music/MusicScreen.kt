package com.soundhub.ui.music

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.soundhub.ui.components.containers.ContentContainer
import com.soundhub.ui.music.components.MusicScreenTabs

@Composable
fun MusicScreen(
    musicViewModel: MusicViewModel = hiltViewModel(),
    navController: NavHostController
) {
    ContentContainer {
        MusicScreenTabs(navController = navController)
    }
}