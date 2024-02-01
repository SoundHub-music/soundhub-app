package com.soundhub.ui.music.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable

@Composable
fun MusicSearchTopBarButton() {
    IconButton(onClick = { /* TODO: write logic for searching music */ }) {
        Icon(Icons.Rounded.Search, contentDescription = null)
    }
}