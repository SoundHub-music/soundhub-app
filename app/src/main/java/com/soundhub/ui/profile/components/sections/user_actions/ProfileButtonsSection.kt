package com.soundhub.ui.profile.components.sections.user_actions

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.soundhub.data.model.User
import com.soundhub.ui.profile.ProfileViewModel

@Composable
internal fun ProfileButtonsSection(
    navController: NavHostController,
    profileViewModel: ProfileViewModel,
    isOriginProfile: Boolean,
    user: User?,
) {
    Row(horizontalArrangement = Arrangement.spacedBy(5.dp)) {
        if (isOriginProfile) {
            EditProfileButton(
                modifier = Modifier.weight(1f),
                navController = navController,
            )
            WritePostButton(navController = navController)
        }
        else {
            WriteMessageButton(
                modifier = Modifier.weight(1f),
                profileViewModel = profileViewModel,
                navController = navController,
                user = user,
            )
            SendFriendRequestButton(
                profileViewModel = profileViewModel,
                user = user
            )
        }
    }
}