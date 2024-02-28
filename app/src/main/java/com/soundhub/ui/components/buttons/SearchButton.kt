package com.soundhub.ui.components.buttons

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import com.soundhub.UiEvent
import com.soundhub.UiStateDispatcher

@Composable
fun SearchButton(uiStateDispatcher: UiStateDispatcher = hiltViewModel()) {
    IconButton(onClick = { uiStateDispatcher.sendUiEvent(UiEvent.SearchButtonClick) }) {
        Icon(Icons.Rounded.Search, contentDescription = null)
    }
}