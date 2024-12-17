package com.soundhub.presentation.shared.bars.top.chatbar.components

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import androidx.navigation.NavHostController
import com.soundhub.Route
import com.soundhub.domain.model.User
import com.soundhub.domain.states.ChatUiState
import com.soundhub.presentation.pages.chat.ChatViewModel
import com.soundhub.presentation.shared.avatar.CircularAvatar

@Composable
internal fun InterlocutorDetails(
	chatViewModel: ChatViewModel,
	navController: NavHostController
) {
	val chatUiState: ChatUiState by chatViewModel.chatUiState.collectAsState()
	val interlocutor: User? = chatUiState.interlocutor

	Row(
		verticalAlignment = Alignment.CenterVertically,
		horizontalArrangement = Arrangement.spacedBy(10.dp),
		modifier = Modifier
			.clip(RoundedCornerShape(5.dp))
			.clickable {
				interlocutor?.let {
					val route = Route.Profile.getStringRouteWithNavArg(it.id.toString())
					Log.d("InterlocutorDetails", route)
					navController.navigate(route)
				}
			}
			.padding(horizontal = 10.dp)
	) {
		CircularAvatar(
			modifier = Modifier.size(40.dp),
			imageUri = interlocutor?.avatarUrl?.toUri()
		)
		InterlocutorFullNameWithOnlineIndicator(chatViewModel)
	}
}