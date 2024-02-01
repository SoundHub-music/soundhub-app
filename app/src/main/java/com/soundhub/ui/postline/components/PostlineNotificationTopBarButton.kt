package com.soundhub.ui.postline.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Notifications
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import com.soundhub.utils.Route

@Composable
fun PostlineNotificationTopBarButton(navController: NavHostController) {
    IconButton(onClick = { navController.navigate(Route.Notifications.route) }) {
        Icon(Icons.Rounded.Notifications, contentDescription = "notification_button")
    }
}