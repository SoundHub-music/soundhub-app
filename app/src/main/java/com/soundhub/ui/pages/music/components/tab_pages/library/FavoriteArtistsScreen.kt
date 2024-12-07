package com.soundhub.ui.pages.music.components.tab_pages.library

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.soundhub.domain.model.Artist
import com.soundhub.ui.pages.music.components.layout.MusicGridLayout
import com.soundhub.ui.pages.music.viewmodels.MusicViewModel

@Composable
fun FavoriteArtistsScreen(musicViewModel: MusicViewModel) {
	val favoriteArtists: List<Artist> by musicViewModel.favoriteArtists.collectAsState()

	LaunchedEffect(key1 = true) {
		musicViewModel.loadUserFavoriteArtists()
	}

	MusicGridLayout(items = favoriteArtists)
}