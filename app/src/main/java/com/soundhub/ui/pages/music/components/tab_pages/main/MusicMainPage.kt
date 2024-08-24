package com.soundhub.ui.pages.music.components.tab_pages.main

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.soundhub.ui.pages.music.MusicViewModel
import com.soundhub.ui.pages.music.components.tab_pages.main.sections.friend_music.FriendMusicSection
import com.soundhub.ui.pages.music.components.tab_pages.main.sections.recommendations.RecommendationSection
import com.soundhub.ui.viewmodels.UiStateDispatcher

@Composable
internal fun MusicMainPage(
	modifier: Modifier = Modifier,
	musicViewModel: MusicViewModel,
	uiStateDispatcher: UiStateDispatcher
) {
	Column(
		modifier = modifier.fillMaxSize(),
		verticalArrangement = Arrangement.spacedBy(10.dp)
	) {
		RecommendationSection(musicViewModel)
		FriendMusicSection(
			musicViewModel = musicViewModel,
			uiStateDispatcher = uiStateDispatcher
		)
	}
}