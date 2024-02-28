package com.soundhub.ui.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.soundhub.data.datastore.UserPreferences
import com.soundhub.ui.authentication.AuthenticationViewModel
import com.soundhub.ui.profile.components.sections.avatar.ProfileUserAvatar
import com.soundhub.ui.profile.components.UserProfileContainer
import java.util.UUID

@Composable
fun ProfileScreen(
    authViewModel: AuthenticationViewModel = hiltViewModel(),
    navController: NavHostController,
    userCreds: UserPreferences? = null,
    userId: UUID? = null,
) {
    Column(
        modifier = Modifier
            .fillMaxHeight()
            .background(MaterialTheme.colorScheme.background),
        // negative indent provides an overlay of the content on the avatar
        verticalArrangement = Arrangement.spacedBy((-30).dp)
    ) {
        ProfileUserAvatar(
            navController = navController,
            authViewModel = authViewModel,
            userCreds = userCreds
        )

        UserProfileContainer(
            user = userId,
            navController = navController
        )
    }
}