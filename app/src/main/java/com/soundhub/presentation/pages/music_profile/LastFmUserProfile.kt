package com.soundhub.presentation.pages.music_profile

import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalConfiguration
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.soundhub.presentation.layout.ProfileLayout
import com.soundhub.presentation.pages.music.viewmodels.LastFmServiceViewModel
import com.soundhub.presentation.pages.music_profile.ui.containers.LastFmProfileContainer
import kotlin.math.floor

const val SHEET_HEIGHT_MODIFIER = 1.65f

@Composable
fun LastFmUserProfile(navController: NavHostController) {
	val viewModel = hiltViewModel<LastFmServiceViewModel>()
	val configuration = LocalConfiguration.current

	val partiallyExpandedSheetHeight = remember {
		derivedStateOf { floor(configuration.screenHeightDp / SHEET_HEIGHT_MODIFIER).toInt() }
	}

	ProfileLayout(
		navController = navController,
		profileViewModel = viewModel,
		sheetHeight = partiallyExpandedSheetHeight.value
	) {
		LastFmProfileContainer(viewModel)
	}
}