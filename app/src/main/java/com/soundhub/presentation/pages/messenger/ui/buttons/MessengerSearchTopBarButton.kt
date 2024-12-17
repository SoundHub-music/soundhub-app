package com.soundhub.presentation.pages.messenger.ui.buttons

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import com.soundhub.data.datastore.UserSettingsStore
import com.soundhub.domain.events.UiEvent
import com.soundhub.presentation.viewmodels.UiStateDispatcher
import kotlinx.coroutines.launch

@Composable
fun MessengerSearchTopBarButton(
	uiStateDispatcher: UiStateDispatcher
) {
	val coroutineScope = rememberCoroutineScope()
	IconButton(onClick = {
		coroutineScope.launch { uiStateDispatcher.sendUiEvent(UiEvent.SearchButtonClick) }
	}) { Icon(Icons.Rounded.Search, contentDescription = "search chat button") }

}


@Composable
@Preview(name = "MessengerSearchButton", showBackground = true)
private fun MessengerSearchTopBarButtonPreview() {
	val context = LocalContext.current
	MessengerSearchTopBarButton(UiStateDispatcher(UserSettingsStore(context)))
}