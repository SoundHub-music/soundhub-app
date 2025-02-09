package com.soundhub.presentation.pages.music.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import com.soundhub.R
import com.soundhub.domain.model.Artist
import com.soundhub.presentation.pages.music.layout.MusicGridLayout
import com.soundhub.presentation.pages.music.viewmodels.MusicViewModel

@Composable
fun FavoriteArtistsScreen(musicViewModel: MusicViewModel) {
	val favoriteArtists: List<Artist> by musicViewModel.favoriteArtists.collectAsState()

	LaunchedEffect(key1 = true) {
		musicViewModel.loadUserFavoriteArtists()
	}

	if (favoriteArtists.isEmpty()) {
		Box(
			modifier = Modifier.fillMaxSize(),
			contentAlignment = Alignment.Center
		) {
			Text(
				text = stringResource(R.string.music_artists_empty),
				modifier = Modifier.fillMaxWidth(0.85f),
				fontSize = 20.sp,
				fontWeight = FontWeight.Bold,
				textAlign = TextAlign.Center
			)
		}
	} else MusicGridLayout(items = favoriteArtists)
}