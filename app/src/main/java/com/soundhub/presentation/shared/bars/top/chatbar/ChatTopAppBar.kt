package com.soundhub.presentation.shared.bars.top.chatbar

import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.KeyboardArrowLeft
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.soundhub.R
import com.soundhub.presentation.pages.chat.ChatViewModel
import com.soundhub.presentation.shared.bars.top.chatbar.actions.ChatTopBarActions
import com.soundhub.presentation.shared.bars.top.chatbar.components.ChatTopAppBarTitle
import com.soundhub.presentation.viewmodels.UiStateDispatcher

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatTopAppBar(
	navController: NavHostController,
	chatViewModel: ChatViewModel,
	uiStateDispatcher: UiStateDispatcher
) {
	val chatUiState by chatViewModel.chatUiState.collectAsState()
	val isCheckMessageModeEnabled = chatUiState.isCheckMessageModeEnabled

	TopAppBar(
		title = {
			ChatTopAppBarTitle(
				chatViewModel = chatViewModel,
				navController = navController
			)
		},
		navigationIcon = {
			IconButton(onClick = {
				if (isCheckMessageModeEnabled) {
					chatViewModel.unsetCheckMessagesMode()
				} else navController.popBackStack()
			}) {
				Icon(
					imageVector = if (isCheckMessageModeEnabled) Icons.Rounded.Close
					else Icons.AutoMirrored.Rounded.KeyboardArrowLeft,
					contentDescription = stringResource(id = R.string.btn_description_back),
					modifier = Modifier.size(28.dp)
				)
			}
		},
		actions = {
			ChatTopBarActions(
				navController = navController,
				chatViewModel = chatViewModel,
				uiStateDispatcher = uiStateDispatcher
			)
		}
	)
}