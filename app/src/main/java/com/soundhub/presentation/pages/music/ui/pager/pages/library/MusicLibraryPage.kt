package com.soundhub.presentation.pages.music.ui.pager.pages.library

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.soundhub.presentation.pages.music.ui.pager.pages.library.components.MusicLibraryMenu
import com.soundhub.presentation.pages.music.ui.pager.pages.library.components.MusicServiceList
import com.soundhub.presentation.pages.music.viewmodels.MusicViewModel

@Composable
internal fun MusicLibraryPage(
	modifier: Modifier = Modifier,
	musicViewModel: MusicViewModel,
	navController: NavHostController
) {
	val authorized = false

	Column(
		modifier = modifier
			.fillMaxSize()
			.padding(top = 20.dp),
		verticalArrangement = Arrangement.SpaceBetween
	) {
		MusicLibraryMenu(
			musicViewModel = musicViewModel,
			navController = navController,
			isAuthorized = authorized
		)
		if (!authorized)
			MusicServiceList()
	}
}