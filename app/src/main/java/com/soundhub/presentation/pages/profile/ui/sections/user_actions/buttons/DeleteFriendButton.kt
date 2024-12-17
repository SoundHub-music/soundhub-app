package com.soundhub.presentation.pages.profile.ui.sections.user_actions.buttons

import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.soundhub.R
import com.soundhub.domain.model.User
import com.soundhub.domain.states.ProfileUiState
import com.soundhub.presentation.pages.profile.ProfileViewModel

@Composable
fun DeleteFriendButton(profileViewModel: ProfileViewModel) {
	val profileUiState: ProfileUiState by profileViewModel.profileUiState.collectAsState()
	val profileOwner: User? = profileUiState.profileOwner

	FilledTonalIconButton(
		modifier = Modifier.size(48.dp),
		shape = RoundedCornerShape(10.dp),
		onClick = { profileViewModel.onDeleteFriendBtnClick(profileOwner) }
	) {
		Icon(
			painter = painterResource(R.drawable.person_remove_24),
			contentDescription = "delete friend button"
		)
	}
}
