package com.soundhub.ui.components.buttons

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import com.soundhub.ui.events.UiEvent
import com.soundhub.ui.viewmodels.UiStateDispatcher
import kotlinx.coroutines.launch

@Composable
fun SearchButton(uiStateDispatcher: UiStateDispatcher) {
    val coroutineScope = rememberCoroutineScope()
    IconButton(onClick = {
        coroutineScope.launch {
            uiStateDispatcher.sendUiEvent(UiEvent.SearchButtonClick)
        }
    }
    ) { Icon(Icons.Rounded.Search, contentDescription = null) }
}