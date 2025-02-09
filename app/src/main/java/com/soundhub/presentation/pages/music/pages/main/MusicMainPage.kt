package com.soundhub.presentation.pages.music.pages.main

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.soundhub.presentation.pages.music.pages.main.sections.friend_music.FriendMusicSection
import com.soundhub.presentation.pages.music.pages.main.sections.recommendations.RecommendationSection
import com.soundhub.presentation.pages.music.viewmodels.MusicViewModel
import com.soundhub.presentation.viewmodels.UiStateDispatcher

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