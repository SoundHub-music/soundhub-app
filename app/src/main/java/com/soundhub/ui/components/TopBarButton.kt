package com.soundhub.ui.components

import androidx.compose.runtime.Composable
import com.soundhub.UiEventDispatcher
import com.soundhub.ui.messenger.components.MessengerSearchTopBarButton
import com.soundhub.ui.music.components.MusicSearchTopBarButton
import com.soundhub.ui.postline.components.PostlineNotificationTopBarButton
import com.soundhub.utils.Route

@Composable
fun TopBarButton(currentRoute: String, uiEventDispatcher: UiEventDispatcher) {
    return when (currentRoute) {
        Route.Postline.route -> PostlineNotificationTopBarButton(uiEventDispatcher)
        Route.Music.route -> MusicSearchTopBarButton()
        Route.Messenger.route -> MessengerSearchTopBarButton()
        else -> {}
    }
}
