package com.soundhub.presentation.pages.music.pages.library

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.soundhub.presentation.pages.music.ui.containers.menu.MusicLibraryMenu
import com.soundhub.presentation.pages.music.ui.containers.music_service.MusicServiceList
import com.soundhub.presentation.pages.music.viewmodels.MusicViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun MusicLibraryPage(
	modifier: Modifier = Modifier,
	musicViewModel: MusicViewModel,
	navController: NavHostController
) {
	Column(
		modifier = modifier
			.fillMaxSize()
			.padding(top = 20.dp),
		verticalArrangement = Arrangement.SpaceBetween
	) {
		MusicLibraryMenu(
			musicViewModel = musicViewModel,
			navController = navController,
		)

		MusicServiceList()
	}
}