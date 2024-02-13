package com.soundhub.ui.music.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import com.soundhub.UiStateDispatcher
import com.soundhub.utils.UiEvent

@Composable
fun MusicSearchTopBarButton(uiStateDispatcher: UiStateDispatcher = hiltViewModel()) {
    IconButton(onClick = { uiStateDispatcher.sendUiEvent(UiEvent.SearchButtonClick) }) {
        Icon(Icons.Rounded.Search, contentDescription = null)
    }
}