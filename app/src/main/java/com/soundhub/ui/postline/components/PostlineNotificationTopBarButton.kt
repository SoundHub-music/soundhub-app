package com.soundhub.ui.postline.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Notifications
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.soundhub.R
import com.soundhub.UiEventDispatcher
import com.soundhub.utils.Route
import com.soundhub.utils.UiEvent

@Composable
fun PostlineNotificationTopBarButton(uiEventDispatcher: UiEventDispatcher) {
    IconButton(onClick = { /*TODO: write logic for notification page*/ }) {
        Icon(Icons.Rounded.Notifications, contentDescription = "notification_button")
    }
}