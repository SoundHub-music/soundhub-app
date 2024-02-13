package com.soundhub.ui.components.bars.top

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.soundhub.UiStateDispatcher
import com.soundhub.ui.messenger.components.MessengerSearchTopBarButton
import com.soundhub.ui.music.components.MusicSearchTopBarButton
import com.soundhub.ui.postline.components.PostlineNotificationTopBarButton
import com.soundhub.utils.Route

@Composable
fun DynamicCustomTopBarButton(
    currentRoute: String?,
    navController: NavHostController,
    uiStateDispatcher: UiStateDispatcher = hiltViewModel()
) {
    return when (currentRoute) {
        Route.Postline.route -> PostlineNotificationTopBarButton(navController)
        Route.Music.route -> MusicSearchTopBarButton(uiStateDispatcher)
        Route.Messenger.route -> MessengerSearchTopBarButton(uiStateDispatcher)
        else -> {}
    }
}
