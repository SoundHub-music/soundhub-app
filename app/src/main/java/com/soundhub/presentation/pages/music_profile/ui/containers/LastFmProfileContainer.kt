package com.soundhub.presentation.pages.music_profile.ui.containers

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ExitToApp
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.soundhub.domain.model.LastFmUser
import com.soundhub.domain.states.IProfileUiState
import com.soundhub.presentation.pages.music.viewmodels.LastFmServiceViewModel
import com.soundhub.presentation.shared.containers.ContentContainer

@Composable
internal fun LastFmProfileContainer(serviceViewModel: LastFmServiceViewModel) {
	val profileUiState: State<IProfileUiState<LastFmUser>> =
		serviceViewModel.profileUiState.collectAsState()
	val profileOwner: LastFmUser? = profileUiState.value.profileOwner

	ContentContainer {
		Row(
			modifier = Modifier
				.padding(top = 20.dp)
				.fillMaxWidth(),
			horizontalArrangement = Arrangement.SpaceBetween
		) {
			Column {
				Text(
					text = profileOwner?.name ?: "",
					style = TextStyle(
						fontSize = 24.sp,
						fontWeight = FontWeight.Black
					)
				)

				Text(
					text = profileOwner?.realName ?: "",
					style = TextStyle(
						fontSize = 16.sp,
						fontWeight = FontWeight.Normal
					)
				)
			}

			Button(
				onClick = { serviceViewModel.logout() },
				modifier = Modifier.padding(start = 10.dp)
			) {
				Icon(Icons.AutoMirrored.Rounded.ExitToApp, contentDescription = "logout")
			}
		}
	}
}