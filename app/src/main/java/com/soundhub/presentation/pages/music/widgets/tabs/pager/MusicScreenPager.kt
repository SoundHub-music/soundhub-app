package com.soundhub.presentation.pages.music.widgets.tabs.pager

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.soundhub.presentation.pages.music.pages.library.MusicLibraryPage
import com.soundhub.presentation.pages.music.pages.main.MusicMainPage
import com.soundhub.presentation.pages.music.viewmodels.MusicViewModel
import com.soundhub.presentation.viewmodels.UiStateDispatcher

@OptIn(ExperimentalFoundationApi::class)
@Composable
internal fun MusicScreenPager(
	pagerState: PagerState,
	musicViewModel: MusicViewModel,
	navController: NavHostController,
	uiStateDispatcher: UiStateDispatcher
) {
	val pages = listOf(
		@Composable {
			MusicMainPage(uiStateDispatcher = uiStateDispatcher)
		}, @Composable {
			MusicLibraryPage(
				musicViewModel = musicViewModel,
				navController = navController
			)
		})

	HorizontalPager(
		state = pagerState,
		modifier = Modifier
			.fillMaxSize()
			.padding(top = 10.dp)
	) { page -> pages[page].invoke() }
}