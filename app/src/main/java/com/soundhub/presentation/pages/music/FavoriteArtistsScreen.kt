package com.soundhub.presentation.pages.music

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.soundhub.domain.model.Artist
import com.soundhub.presentation.pages.music.ui.layout.MusicGridLayout
import com.soundhub.presentation.pages.music.viewmodels.MusicViewModel

@Composable
fun FavoriteArtistsScreen(musicViewModel: MusicViewModel) {
	val favoriteArtists: List<Artist> by musicViewModel.favoriteArtists.collectAsState()

	LaunchedEffect(key1 = true) {
		musicViewModel.loadUserFavoriteArtists()
	}

	MusicGridLayout(items = favoriteArtists)
}