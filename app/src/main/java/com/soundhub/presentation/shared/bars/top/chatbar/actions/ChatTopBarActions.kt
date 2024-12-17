package com.soundhub.presentation.shared.bars.top.chatbar.actions

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.MoreVert
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.navigation.NavHostController
import com.soundhub.presentation.pages.chat.ChatViewModel
import com.soundhub.presentation.shared.menu.ChatTopBarDropdownMenu
import com.soundhub.presentation.viewmodels.UiStateDispatcher

@Composable
internal fun ChatTopBarActions(
	navController: NavHostController,
	chatViewModel: ChatViewModel,
	uiStateDispatcher: UiStateDispatcher
) {
	val isMenuExpanded: MutableState<Boolean> = rememberSaveable { mutableStateOf(false) }
	val chatUiState by chatViewModel.chatUiState.collectAsState()
	val isCheckMessagesModeEnabled = chatUiState.isCheckMessageModeEnabled

	if (isCheckMessagesModeEnabled)
		ChatTopBarCheckMessagesActions(
			chatViewModel = chatViewModel,
			uiStateDispatcher = uiStateDispatcher
		)
	else {
		IconButton(onClick = { isMenuExpanded.value = !isMenuExpanded.value }) {
			Icon(imageVector = Icons.Rounded.MoreVert, contentDescription = "options")
		}

		ChatTopBarDropdownMenu(
			menuState = isMenuExpanded,
			navController = navController,
			chatViewModel = chatViewModel,
			uiStateDispatcher = uiStateDispatcher
		)
	}
}