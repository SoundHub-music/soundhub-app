package com.soundhub.ui.profile.components.sections.avatar

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.soundhub.R
import com.soundhub.UiEventDispatcher
import com.soundhub.ui.authentication.AuthenticationViewModel
import com.soundhub.utils.Route
import com.soundhub.utils.UiEvent

@Composable
fun UserAvatar() {
    val avatar: Painter = painterResource(id = R.drawable.header)
    val authViewModel: AuthenticationViewModel = hiltViewModel()
    val uiEventDispatcher: UiEventDispatcher = hiltViewModel()

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(0.45f)
    ) {
        Image(
            painter = avatar, contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.matchParentSize()
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 20.dp, start = 16.dp, end = 16.dp),
            horizontalArrangement = Arrangement.End
        ) {
            IconButton(onClick = {
            /* TODO make onClick logic for settings page */
                uiEventDispatcher.sendUiEvent(UiEvent.Navigate(Route.Settings))

            }) {
                Icon(imageVector = Icons.Rounded.Settings, contentDescription = null)
            }
        }
    }
}