package com.soundhub.ui.shared.bars.top.chatbar.components

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.soundhub.R
import com.soundhub.ui.pages.chat.ChatViewModel

@Composable
internal fun ChatTopAppBarTitle(
	chatViewModel: ChatViewModel,
	navController: NavHostController,
) {
	val chatUiState by chatViewModel.chatUiState.collectAsState()
	val isCheckMessageModeEnabled = chatUiState.isCheckMessageModeEnabled
	val checkedMessagesCount: Int = chatUiState.checkedMessages.size

	if (!isCheckMessageModeEnabled)
		InterlocutorDetails(chatViewModel, navController)
	else Text(
		text = stringResource(id = R.string.chat_check_mode_count, checkedMessagesCount),
		fontSize = 16.sp
	)
}