package com.soundhub.ui.pages.music.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.soundhub.ui.pages.music.MusicViewModel
import com.soundhub.ui.pages.music.components.tab_pages.MusicLibraryPage
import com.soundhub.ui.pages.music.components.tab_pages.MusicMainPage
import com.soundhub.ui.viewmodels.UiStateDispatcher

@OptIn(ExperimentalFoundationApi::class)
@Composable
internal fun MusicScreenPager(
    pagerState: PagerState,
    musicViewModel: MusicViewModel,
    navController: NavHostController,
    uiStateDispatcher: UiStateDispatcher
) {
    HorizontalPager(
        state = pagerState,
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 10.dp)
    ) { page ->
        when (page) {
            0 -> MusicMainPage(
                musicViewModel = musicViewModel,
                uiStateDispatcher = uiStateDispatcher
            )
            1 -> MusicLibraryPage(
                musicViewModel = musicViewModel,
                navController = navController
            )
        }
    }
}