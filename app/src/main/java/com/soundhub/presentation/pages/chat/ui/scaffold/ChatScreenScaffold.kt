package com.soundhub.presentation.pages.chat.ui.scaffold

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import com.soundhub.presentation.pages.chat.ChatViewModel
import com.soundhub.presentation.shared.bars.top.chatbar.ChatTopAppBar
import com.soundhub.presentation.viewmodels.UiStateDispatcher

@Composable
internal fun ChatScreenScaffold(
	chatViewModel: ChatViewModel,
	navController: NavHostController,
	uiStateDispatcher: UiStateDispatcher,
	content: @Composable (PaddingValues) -> Unit
) {
	Scaffold(
		topBar = {
			ChatTopAppBar(
				navController = navController,
				chatViewModel = chatViewModel,
				uiStateDispatcher = uiStateDispatcher
			)
		}
	) { content(it) }
}