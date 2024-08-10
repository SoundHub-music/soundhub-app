package com.soundhub.ui.pages.chat

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
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
import com.soundhub.data.model.Message
import com.soundhub.data.model.User
import com.soundhub.data.states.ChatUiState
import com.soundhub.data.states.UiState
import com.soundhub.ui.pages.chat.components.input_box.MessageInputBox
import com.soundhub.ui.pages.chat.components.message_box_list.MessageBoxList
import com.soundhub.ui.shared.bars.top.ChatTopAppBar
import com.soundhub.ui.shared.containers.ContentContainer
import com.soundhub.ui.viewmodels.UiStateDispatcher
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
	val uiState: UiState by uiStateDispatcher.uiState.collectAsState(initial = UiState())
	val isCheckMessageModeEnabled: Boolean = chatUiState.isCheckMessageModeEnabled

	val authorizedUser: User? = uiState.authorizedUser
	val messages: List<Message> = chatUiState.chat?.messages.orEmpty()

	val lazyListState: LazyListState = rememberLazyListState()
	val firstVisibleMessageIndex: Int by remember { derivedStateOf { lazyListState.firstVisibleItemIndex } }

	BackHandler {
		if (isCheckMessageModeEnabled)
			chatViewModel.unsetCheckMessagesMode()
		else navController.popBackStack()
	}

	LaunchedEffect(firstVisibleMessageIndex, messages, authorizedUser) {
		var index = firstVisibleMessageIndex
		if (firstVisibleMessageIndex > 0)
			index -= 1

		chatViewModel.readVisibleMessagesFromIndex(index)
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
				verticalArrangement = Arrangement.SpaceBetween,
				modifier = Modifier.padding(bottom = 10.dp)
			) {
				MessageBoxList(
					lazyListState = lazyListState,
					chatViewModel = chatViewModel,
					modifier = Modifier.weight(1f),
					uiStateDispatcher = uiStateDispatcher
				)
				MessageInputBox(
					lazyListState = lazyListState,
					modifier = Modifier,
					chatViewModel = chatViewModel,
				)
			}
		}
	}
}

@Composable
private fun ChatScreenScaffold(
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
