package com.soundhub.presentation.pages.profile.ui.sections.avatar

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.bumptech.glide.integration.compose.placeholder
import com.soundhub.R
import com.soundhub.Route
import com.soundhub.domain.model.User
import com.soundhub.domain.states.ProfileUiState
import com.soundhub.presentation.pages.profile.ProfileViewModel
import com.soundhub.presentation.shared.avatar.AvatarViewModel
import com.soundhub.presentation.shared.menu.AvatarDropdownMenu

@Composable
internal fun UserProfileAvatar(
	navController: NavHostController,
	profileViewModel: ProfileViewModel,
) {
	var selectedImageUri: Uri? by rememberSaveable { mutableStateOf(null) }
	val isAvatarMenuExpandedState = rememberSaveable { mutableStateOf(false) }
	val changeAvatarLauncher = rememberLauncherForActivityResult(
		contract = ActivityResultContracts.GetContent()
	) { uri -> uri?.let { selectedImageUri = it } }

	Box(
		modifier = Modifier
			.fillMaxWidth()
			.fillMaxHeight(0.45f)
	) {
		Avatar(
			isAvatarMenuExpandedState = isAvatarMenuExpandedState,
			profileViewModel = profileViewModel
		)
		AvatarDropdownMenu(
			modifier = Modifier.align(Alignment.Center),
			isAvatarMenuExpandedState = isAvatarMenuExpandedState,
			onDismissRequest = { isAvatarMenuExpandedState.value = false },
			activityResultLauncher = changeAvatarLauncher
		)
		UserSettingsButton(navController)
	}
}

@Composable
private fun UserSettingsButton(
	navController: NavHostController
) {
	Row(
		modifier = Modifier
			.fillMaxWidth()
			.padding(top = 20.dp, start = 16.dp, end = 16.dp),
		horizontalArrangement = Arrangement.End
	) {
		IconButton(onClick = { navController.navigate(Route.Settings.route) }) {
			Icon(imageVector = Icons.Rounded.Settings, contentDescription = null)
		}
	}
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
private fun Avatar(
	isAvatarMenuExpandedState: MutableState<Boolean>,
	avatarViewModel: AvatarViewModel = hiltViewModel(),
	profileViewModel: ProfileViewModel
) {
	val profileUiState: ProfileUiState by profileViewModel.profileUiState.collectAsState()
	val profileOwner: User? = profileUiState.profileOwner

	val userFullName: String = profileOwner?.getFullName() ?: ""
	var glideUrl: Any? by remember { mutableStateOf(null) }
	val defaultAvatar: Painter = painterResource(id = R.drawable.circular_user)

	LaunchedEffect(key1 = profileOwner) {
		glideUrl = avatarViewModel.getGlideUrlOrImageUri(profileOwner?.avatarUrl?.toUri())
	}

	GlideImage(
		model = glideUrl,
		contentScale = ContentScale.Crop,
		failure = placeholder(defaultAvatar),
		contentDescription = userFullName,
		modifier = Modifier
			.fillMaxSize()
			.clickable {
				isAvatarMenuExpandedState.value = true
			},
	)
}