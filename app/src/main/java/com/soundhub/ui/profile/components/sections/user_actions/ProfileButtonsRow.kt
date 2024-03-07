package com.soundhub.ui.profile.components.sections.user_actions

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.soundhub.data.model.User

@Composable
internal fun ProfileButtonsRow(
    isOriginProfile: Boolean,
    user: User?,
    navController: NavHostController
) {
    Row(horizontalArrangement = Arrangement.spacedBy(5.dp)) {
        WriteMessageOrEditProfileButton(
            isOriginProfile = isOriginProfile,
            navController = navController,
            modifier = Modifier.weight(1f)
        )
        if (isOriginProfile)
            WritePostButton(navController = navController)
        else
            SendFriendRequestButton()
    }
}