package com.soundhub.ui.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.soundhub.data.model.User
import com.soundhub.ui.components.loaders.CircleLoader
import com.soundhub.ui.profile.components.sections.avatar.UserProfileAvatar
import com.soundhub.ui.profile.components.UserProfileContainer
import com.soundhub.ui.states.UiState
import com.soundhub.ui.viewmodels.UiStateDispatcher
import java.util.UUID

@Composable
fun ProfileScreen(
    navController: NavHostController,
    userId: UUID?,
    uiStateDispatcher: UiStateDispatcher,
    profileViewModel: ProfileViewModel,
) {
    val uiState: UiState by uiStateDispatcher.uiState.collectAsState(initial = UiState())
    val authorizedUser: User? = uiState.authorizedUser
    val profileUiState by profileViewModel.profileUiState.collectAsState()
    val profileOwner: User? = profileUiState.profileOwner
    val isLoading: Boolean = remember(profileOwner) {
        profileOwner == null
    }

    LaunchedEffect(key1 = userId, key2 = authorizedUser) {
        userId?.let { userId -> profileViewModel.loadProfileOwner(userId) }
    }

    Column(
        modifier = Modifier
            .fillMaxHeight()
            .background(MaterialTheme.colorScheme.background),
        // negative indent provides an overlay of the content on the avatar
        verticalArrangement = Arrangement.spacedBy((-30).dp)
    ) {
        if (isLoading)
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                CircleLoader(
                    modifier = Modifier.size(72.dp),
                    strokeWidth = 6.dp
                )
            }
        else {
            UserProfileAvatar(
                navController = navController,
                uiStateDispatcher = uiStateDispatcher,
                profileViewModel = profileViewModel
            )

            UserProfileContainer(
                navController = navController,
                uiStateDispatcher = uiStateDispatcher,
                profileViewModel = profileViewModel
            )
        }
    }
}