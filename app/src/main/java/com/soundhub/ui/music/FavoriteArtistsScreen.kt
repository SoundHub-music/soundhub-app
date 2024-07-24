package com.soundhub.ui.music

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.soundhub.data.model.Artist
import com.soundhub.ui.music.components.layout.MusicGridLayout

@Composable
fun FavoriteArtistsScreen(musicViewModel: MusicViewModel) {
    val favoriteArtists: List<Artist> by musicViewModel.favoriteArtists.collectAsState()
    MusicGridLayout(items = favoriteArtists)
}