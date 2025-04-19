package com.soundhub.presentation.pages.profile

import android.util.Log
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.soundhub.domain.states.UiState
import com.soundhub.presentation.layout.ProfileLayout
import com.soundhub.presentation.pages.profile.ui.UserProfileContainer
import com.soundhub.presentation.viewmodels.UiStateDispatcher
import java.util.UUID

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
	navController: NavHostController,
	userId: UUID?,
	uiStateDispatcher: UiStateDispatcher,
	profileViewModel: ProfileViewModel = hiltViewModel(),
) {
	val uiState by uiStateDispatcher.uiState.collectAsState(initial = UiState())

	LaunchedEffect(key1 = userId) {
		Log.d("ProfileScreen", "userId: $userId")
		userId?.let { profileViewModel.loadProfileOwner(it) }
	}

	// update profile owner if authorized user instance has been changed
	LaunchedEffect(key1 = uiState.authorizedUser) {
		if (userId != uiState.authorizedUser?.id)
			return@LaunchedEffect

		val authorizedUser = uiState.authorizedUser

		authorizedUser?.let {
			profileViewModel.loadProfileOwner(it.id)
		}
	}

	ProfileLayout(
		navController = navController,
		profileViewModel = profileViewModel
	) {
		UserProfileContainer(
			navController = navController,
			uiStateDispatcher = uiStateDispatcher,
			profileViewModel = profileViewModel
		)
	}
}

