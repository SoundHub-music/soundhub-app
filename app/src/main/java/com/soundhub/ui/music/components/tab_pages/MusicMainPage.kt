package com.soundhub.ui.music.components.tab_pages

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.soundhub.ui.music.components.sections.recommendations.FriendMusicSection
import com.soundhub.ui.music.components.sections.recommendations.RecommendationSection

@Composable
internal fun MusicMainPage(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        RecommendationSection()
        FriendMusicSection()
    }
}