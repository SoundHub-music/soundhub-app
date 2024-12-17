package com.soundhub.presentation.pages.profile.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.soundhub.domain.model.User
import com.soundhub.domain.states.ProfileUiState
import com.soundhub.domain.states.UiState
import com.soundhub.presentation.pages.profile.ProfileViewModel
import com.soundhub.presentation.pages.profile.ui.containers.friend_miniatures.FriendMiniatureSection
import com.soundhub.presentation.pages.profile.ui.sections.favorite_genres.FavoriteGenresSection
import com.soundhub.presentation.pages.profile.ui.sections.user_actions.ProfileButtonsSection
import com.soundhub.presentation.pages.profile.ui.sections.user_main_data.UserMainDataSection
import com.soundhub.presentation.pages.profile.ui.sections.wall.UserWall
import com.soundhub.presentation.viewmodels.UiStateDispatcher

@Composable
fun UserProfileContainer(
	navController: NavHostController,
	uiStateDispatcher: UiStateDispatcher,
	profileViewModel: ProfileViewModel
) {
	val uiState: UiState by uiStateDispatcher.uiState.collectAsState(initial = UiState())
	val profileUiState: ProfileUiState by profileViewModel.profileUiState.collectAsState()

	val authorizedUser: User? = uiState.authorizedUser
	val profileOwner: User? = profileUiState.profileOwner
	var isOriginProfile: Boolean by remember {
		mutableStateOf(authorizedUser?.id == profileOwner?.id)
	}

	LaunchedEffect(key1 = authorizedUser, key2 = profileOwner) {
		isOriginProfile = authorizedUser?.id == profileOwner?.id
		profileViewModel.loadPostsByUser()
	}

	LazyColumn(
		modifier = Modifier
			.fillMaxSize()
			.background(
				color = MaterialTheme.colorScheme.primaryContainer,
				shape = RoundedCornerShape(topStart = 30.dp, topEnd = 30.dp)
			)
			.padding(16.dp),

		verticalArrangement = Arrangement.spacedBy(8.dp)
	) {
		item { UserMainDataSection(profileViewModel) }

		item {
			ProfileButtonsSection(
				profileViewModel = profileViewModel,
				uiStateDispatcher = uiStateDispatcher,
				isOriginProfile = isOriginProfile,
				navController = navController,
			)
		}

		item { FriendMiniatureSection(profileViewModel) }

		item {
			FavoriteGenresSection(
				profileViewModel = profileViewModel,
				isOriginProfile = isOriginProfile,
				navController = navController
			)
		}

		item {
			HorizontalDivider(thickness = 1.dp)
			UserWall(
				navController = navController,
				uiStateDispatcher = uiStateDispatcher,
				profileViewModel = profileViewModel,
			)
		}
	}
}
