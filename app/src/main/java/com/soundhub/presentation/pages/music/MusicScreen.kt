package com.soundhub.presentation.pages.music

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import com.soundhub.R
import com.soundhub.presentation.pages.music.ui.MusicScreenTabRow
import com.soundhub.presentation.pages.music.ui.pager.MusicScreenPager
import com.soundhub.presentation.pages.music.viewmodels.MusicViewModel
import com.soundhub.presentation.shared.containers.ContentContainer
import com.soundhub.presentation.viewmodels.UiStateDispatcher

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MusicScreen(
	musicViewModel: MusicViewModel,
	navController: NavHostController,
	uiStateDispatcher: UiStateDispatcher
) {
	val tabs: List<String> = listOf(
		stringResource(id = R.string.music_main_page),
		stringResource(id = R.string.music_library_page)
	)

	val pagerState: PagerState = rememberPagerState(
		initialPage = 0,
		pageCount = { tabs.size }
	)

	Column {
		MusicScreenTabRow(
			pagerState = pagerState,
			tabs = tabs
		)
		ContentContainer(modifier = Modifier) {
			MusicScreenPager(
				pagerState = pagerState,
				navController = navController,
				musicViewModel = musicViewModel,
				uiStateDispatcher = uiStateDispatcher
			)
		}
	}
}