package com.soundhub.ui.pages.messenger

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import com.soundhub.ui.pages.messenger.components.MessengerChatList
import com.soundhub.ui.shared.containers.ContentContainer
import com.soundhub.ui.viewmodels.UiStateDispatcher

@Composable
fun MessengerScreen(
	navController: NavHostController,
	uiStateDispatcher: UiStateDispatcher,
	messengerViewModel: MessengerViewModel
) {
	ContentContainer {
		MessengerChatList(
			navController = navController,
			uiStateDispatcher = uiStateDispatcher,
			messengerViewModel = messengerViewModel
		)
	}
}