package com.soundhub.presentation.shared.bars.top.chatbar.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.soundhub.R
import com.soundhub.presentation.pages.chat.ChatViewModel

@Composable
internal fun ChatTopAppBarTitle(
	chatViewModel: ChatViewModel,
	navController: NavHostController,
) {
	val chatUiState by chatViewModel.chatUiState.collectAsState()
	val isCheckMessageModeEnabled = chatUiState.isCheckMessageModeEnabled
	val checkedMessagesCount: Int = chatUiState.checkedMessages.size

	Box(modifier = Modifier.fillMaxSize()) {
		AnimatedVisibility(
			visible = !isCheckMessageModeEnabled,
			enter = fadeIn(),
			exit = fadeOut(),
		) {
			InterlocutorDetails(chatViewModel, navController)
		}

		AnimatedVisibility(
			visible = isCheckMessageModeEnabled,
			enter = fadeIn(),
			exit = fadeOut(),
			modifier = Modifier.align(Alignment.CenterStart)
		) {
			Text(
				text = stringResource(id = R.string.chat_check_mode_count, checkedMessagesCount),
				fontSize = 16.sp
			)
		}
	}
}