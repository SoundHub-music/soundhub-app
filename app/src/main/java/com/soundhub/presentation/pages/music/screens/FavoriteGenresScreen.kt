package com.soundhub.presentation.pages.music.screens

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.soundhub.domain.model.Genre
import com.soundhub.presentation.pages.music.layout.MusicGridLayout
import com.soundhub.presentation.pages.music.viewmodels.MusicViewModel

@Composable
fun FavoriteGenresScreen(musicViewModel: MusicViewModel) {
	val favoriteGenres: List<Genre> by musicViewModel.favoriteGenres.collectAsState()

	LaunchedEffect(key1 = true) {
		musicViewModel.loadUserFavoriteGenres()
	}

	MusicGridLayout(items = favoriteGenres)
}