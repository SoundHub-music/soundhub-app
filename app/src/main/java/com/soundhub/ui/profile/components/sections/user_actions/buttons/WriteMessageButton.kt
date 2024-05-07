package com.soundhub.ui.profile.components.sections.user_actions.buttons

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.MailOutline
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import com.soundhub.R
import com.soundhub.Route
import com.soundhub.data.model.Chat
import com.soundhub.data.model.User
import com.soundhub.ui.profile.ProfileViewModel
import kotlinx.coroutines.launch

@Composable
internal fun WriteMessageButton(
    modifier: Modifier = Modifier,
    profileViewModel: ProfileViewModel,
    navController: NavHostController,
    user: User?
) {
    val coroutineScope = rememberCoroutineScope()
    val buttonContent = stringResource(id = R.string.profile_screen_write_message_btn_content)
    val buttonIcon: ImageVector = Icons.Rounded.MailOutline

    ProfileActionLongButton(
        content = buttonContent,
        buttonIcon = buttonIcon,
        modifier = modifier
    ) {
        coroutineScope.launch {
            onButtonClick(
                profileViewModel = profileViewModel,
                navController = navController,
                user = user
            )
        }
    }
}

private suspend fun onButtonClick(
    profileViewModel: ProfileViewModel,
    navController: NavHostController,
    user: User?
) {
    val chat: Chat? = profileViewModel
        .getOrCreateChatByUser(user)

    chat?.let {
        navController.navigate(
            Route.Messenger.Chat
                .getStringRouteWithNavArg(it.id.toString())
        )
    }
}