package com.soundhub.ui.messenger

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import com.soundhub.ui.viewmodels.UiStateDispatcher
import com.soundhub.ui.components.containers.ContentContainer
import com.soundhub.ui.messenger.components.MessengerChatList

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