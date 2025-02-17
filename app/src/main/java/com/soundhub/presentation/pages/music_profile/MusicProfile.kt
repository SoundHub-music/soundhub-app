package com.soundhub.presentation.pages.music_profile

import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalConfiguration
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.soundhub.presentation.layout.ProfileLayout
import com.soundhub.presentation.pages.music_profile.ui.containers.ProfileContainer
import kotlin.math.floor

@Composable
fun MusicProfile(
	navController: NavHostController
) {
	val configuration = LocalConfiguration.current
	val partiallyExpandedSheetHeight = remember {
		derivedStateOf { floor(configuration.screenHeightDp / 1.65).toInt() }
	}

	val profileViewModel: MusicProfileViewModel = hiltViewModel()

	ProfileLayout(
		navController = navController,
		profileViewModel = profileViewModel,
		sheetHeight = partiallyExpandedSheetHeight.value
	) {
		ProfileContainer(profileViewModel)
	}
}