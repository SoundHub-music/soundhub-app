package com.soundhub.ui.music.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.soundhub.ui.music.components.tab_pages.MusicLibraryPage
import com.soundhub.ui.music.components.tab_pages.MusicMainPage

@OptIn(ExperimentalFoundationApi::class)
@Composable
internal fun MusicScreenPager(selectedTabState: PagerState, navController: NavHostController) {
    HorizontalPager(
        state = selectedTabState,
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 10.dp)
    ) { page ->
        when (page) {
            0 -> MusicMainPage(navController = navController)
            1 -> MusicLibraryPage(navController = navController)
        }
    }
}