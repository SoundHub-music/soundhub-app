package com.soundhub.ui.music.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import com.soundhub.UiEventDispatcher

@Composable
fun MusicSearchTopBarButton(uiEventDispatcher: UiEventDispatcher = hiltViewModel()) {
    IconButton(onClick = { /* TODO: write logic for searching music */ }) {
        Icon(Icons.Rounded.Search, contentDescription = null)
    }
}