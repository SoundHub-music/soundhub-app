package com.soundhub.ui.profile.components.sections.user_actions

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.soundhub.data.datastore.UserPreferences

@Composable
internal fun ProfileButtonsRow(
    isOriginProfile: Boolean,
    user: UserPreferences?,
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