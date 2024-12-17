package com.soundhub.presentation.pages.messenger

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import com.soundhub.presentation.pages.messenger.ui.containers.MessengerChatList
import com.soundhub.presentation.shared.containers.ContentContainer
import com.soundhub.presentation.viewmodels.UiStateDispatcher

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