package com.soundhub.presentation.pages.settings.ui.cards

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.net.toUri
import com.soundhub.domain.model.User
import com.soundhub.presentation.pages.authentication.AuthenticationViewModel
import com.soundhub.presentation.pages.settings.ui.buttons.LogoutButton
import com.soundhub.presentation.shared.avatar.CircularAvatar

@Composable
internal fun UserCardSettings(
	authViewModel: AuthenticationViewModel,
) {
	var authorizedUser = rememberSaveable { mutableStateOf<User?>(null) }
	val userFullName: String = authorizedUser.value?.getFullName() ?: ""

	LaunchedEffect(true) {
		authorizedUser.value = authViewModel.getAuthorizedUser()
	}

	Box(
		modifier = Modifier
			.fillMaxWidth()
			.padding(bottom = 15.dp)
	) {
		Row(
			horizontalArrangement = Arrangement.SpaceBetween,
			verticalAlignment = Alignment.CenterVertically,
			modifier = Modifier.fillMaxWidth()
		) {
			Row(
				verticalAlignment = Alignment.CenterVertically,
				horizontalArrangement = Arrangement.spacedBy(10.dp)
			) {
				CircularAvatar(
					modifier = Modifier.size(64.dp),
					imageUri = authorizedUser.value?.avatarUrl?.toUri()
				)
				Text(
					text = userFullName,
					fontWeight = FontWeight.Bold,
					fontSize = 18.sp,
					lineHeight = 32.sp
				)
			}
			LogoutButton(authViewModel)
		}
	}
}