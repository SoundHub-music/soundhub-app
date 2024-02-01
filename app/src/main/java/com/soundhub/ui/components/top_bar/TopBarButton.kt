package com.soundhub.ui.components.top_bar

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import com.soundhub.ui.messenger.components.MessengerSearchTopBarButton
import com.soundhub.ui.music.components.MusicSearchTopBarButton
import com.soundhub.ui.postline.components.PostlineNotificationTopBarButton
import com.soundhub.utils.Route

@Composable
fun TopBarButton(currentRoute: String?, navController: NavHostController) {
    return when (currentRoute) {
        Route.Postline.route -> PostlineNotificationTopBarButton(navController)
        Route.Music.route -> MusicSearchTopBarButton()
        Route.Messenger.route -> MessengerSearchTopBarButton()
        else -> {}
    }
}
