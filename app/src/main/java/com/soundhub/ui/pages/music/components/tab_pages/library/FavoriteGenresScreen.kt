package com.soundhub.ui.pages.music.components.tab_pages.library

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.soundhub.data.model.Genre
import com.soundhub.ui.pages.music.MusicViewModel
import com.soundhub.ui.pages.music.components.layout.MusicGridLayout

@Composable
fun FavoriteGenresScreen(musicViewModel: MusicViewModel) {
	val favoriteGenres: List<Genre> by musicViewModel.favoriteGenres.collectAsState()

	LaunchedEffect(key1 = true) {
		musicViewModel.loadUserFavoriteGenres()
	}

	MusicGridLayout(items = favoriteGenres)
}