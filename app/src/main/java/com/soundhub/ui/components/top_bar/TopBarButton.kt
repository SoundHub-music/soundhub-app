package com.soundhub.ui.components.top_bar

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.soundhub.UiEventDispatcher
import com.soundhub.ui.messenger.components.MessengerSearchTopBarButton
import com.soundhub.ui.music.components.MusicSearchTopBarButton
import com.soundhub.ui.postline.components.PostlineNotificationTopBarButton
import com.soundhub.utils.Route

@Composable
fun TopBarButton(
    currentRoute: String?,
    navController: NavHostController,
    uiEventDispatcher: UiEventDispatcher = hiltViewModel()
) {
    return when (currentRoute) {
        Route.Postline.route -> PostlineNotificationTopBarButton(navController)
        Route.Music.route -> MusicSearchTopBarButton(uiEventDispatcher)
        Route.Messenger.route -> MessengerSearchTopBarButton(uiEventDispatcher)
        else -> Unit
    }
}
