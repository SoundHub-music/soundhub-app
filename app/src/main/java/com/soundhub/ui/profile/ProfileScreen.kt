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
import com.soundhub.data.model.User
import com.soundhub.ui.authentication.AuthenticationViewModel
import com.soundhub.ui.profile.components.sections.avatar.UserProfileAvatar
import com.soundhub.ui.profile.components.UserProfileContainer

@Composable
fun ProfileScreen(
    authViewModel: AuthenticationViewModel = hiltViewModel(),
    navController: NavHostController,
    user: User? = null,
) {
    Column(
        modifier = Modifier
            .fillMaxHeight()
            .background(MaterialTheme.colorScheme.background),
        // negative indent provides an overlay of the content on the avatar
        verticalArrangement = Arrangement.spacedBy((-30).dp)
    ) {
        UserProfileAvatar(
            navController = navController,
            authViewModel = authViewModel,
            user = user
        )

        UserProfileContainer(
            user = user,
            navController = navController
        )
    }
}