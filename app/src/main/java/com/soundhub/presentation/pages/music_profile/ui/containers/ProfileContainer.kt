package com.soundhub.presentation.pages.music_profile.ui.containers

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.soundhub.domain.model.LastFmUser
import com.soundhub.presentation.pages.music_profile.MusicProfileViewModel
import com.soundhub.presentation.shared.containers.ContentContainer

@Composable
internal fun ProfileContainer(profileViewModel: MusicProfileViewModel) {
	val profileUiState = profileViewModel.profileUiState.collectAsState()
	val profileOwner: LastFmUser? = profileUiState.value.profileOwner

	ContentContainer {
		Row {
			Column {
				profileOwner?.name?.let {
					Text(
						text = it,
						style = TextStyle(
							fontSize = 24.sp,
							fontWeight = FontWeight.Black
						)
					)
				}

				profileOwner?.realName?.let {
					Text(
						text = it,
						style = TextStyle(
							fontSize = 16.sp,
							fontWeight = FontWeight.Normal
						)
					)
				}
			}
		}
	}
}