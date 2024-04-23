package com.soundhub.ui.messenger.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.tooling.preview.Preview
import com.soundhub.ui.viewmodels.UiStateDispatcher
import com.soundhub.ui.events.UiEvent
import kotlinx.coroutines.launch

@Composable
fun MessengerSearchTopBarButton(
    uiStateDispatcher: UiStateDispatcher 
) {
    val coroutineScope = rememberCoroutineScope()
    IconButton(onClick = {
        coroutineScope.launch {
            uiStateDispatcher.sendUiEvent(UiEvent.SearchButtonClick)
        }
    }) {
        Icon(Icons.Rounded.Search, contentDescription = null)
    }

}


@Composable
@Preview(name = "MessengerSearchButton", showBackground = true)
fun MessengerSearchTopBarButtonPreview() {
    MessengerSearchTopBarButton(UiStateDispatcher())
}