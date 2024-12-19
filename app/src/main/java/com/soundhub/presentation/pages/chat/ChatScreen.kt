package com.soundhub.presentation.pages.chat

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.soundhub.R
import com.soundhub.domain.states.ChatUiState
import com.soundhub.presentation.pages.chat.ui.containers.message_list.MessageBoxList
import com.soundhub.presentation.pages.chat.ui.input.MessageInputBox
import com.soundhub.presentation.pages.chat.ui.scaffold.ChatScreenScaffold
import com.soundhub.presentation.shared.containers.ContentContainer
import com.soundhub.presentation.viewmodels.UiStateDispatcher
import java.util.UUID

@Composable
fun ChatScreen(
	chatId: UUID,
	chatViewModel: ChatViewModel = hiltViewModel(),
	uiStateDispatcher: UiStateDispatcher,
	navController: NavHostController
) {
	val backgroundImage: Painter = painterResource(id = R.drawable.chat_background)
	val chatUiState: ChatUiState by chatViewModel.chatUiState.collectAsState()
	val isCheckMessageModeEnabled: Boolean = chatUiState.isCheckMessageModeEnabled

	val lazyListState: LazyListState = rememberLazyListState()

	BackHandler {
		if (isCheckMessageModeEnabled)
			chatViewModel.unsetCheckMessagesMode()
		else navController.popBackStack()
	}

	LaunchedEffect(key1 = chatId) {
		chatViewModel.loadChatById(chatId)
	}

	ChatScreenScaffold(
		chatViewModel = chatViewModel,
		navController = navController,
		uiStateDispatcher = uiStateDispatcher
	) {
		ContentContainer(
			modifier = Modifier
				.paint(painter = backgroundImage, contentScale = ContentScale.Crop)
				.background(Color.Transparent)
				.padding(it)
				.padding(top = 10.dp),
		) {
			Column(
				verticalArrangement = Arrangement.spacedBy(10.dp),
				modifier = Modifier.padding(bottom = 10.dp)
			) {
				MessageBoxList(
					chatViewModel = chatViewModel,
					modifier = Modifier.weight(1f),
					uiStateDispatcher = uiStateDispatcher,
					lazyListState = lazyListState
				)
				MessageInputBox(
					modifier = Modifier,
					chatViewModel = chatViewModel,
					lazyListState = lazyListState
				)
			}
		}
	}
}