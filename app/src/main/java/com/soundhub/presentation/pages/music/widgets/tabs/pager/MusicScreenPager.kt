package com.soundhub.presentation.pages.music.widgets.tabs.pager

import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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
	val pagerLockState by musicViewModel.pagerLockState.collectAsState()

	LaunchedEffect(key1 = pagerLockState) {
		Log.d("MusicScreenPager", pagerLockState.toString())
	}

	HorizontalPager(
		state = pagerState,
		userScrollEnabled = !pagerLockState,
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