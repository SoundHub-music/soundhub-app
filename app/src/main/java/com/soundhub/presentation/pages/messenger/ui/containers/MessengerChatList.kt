package com.soundhub.presentation.pages.messenger.ui.containers

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.soundhub.data.enums.ApiStatus
import com.soundhub.domain.model.Chat
import com.soundhub.domain.model.Message
import com.soundhub.domain.model.User
import com.soundhub.domain.states.MessengerUiState
import com.soundhub.domain.states.UiState
import com.soundhub.presentation.pages.messenger.MessengerViewModel
import com.soundhub.presentation.pages.messenger.ui.cards.chat_card.ChatCard
import com.soundhub.presentation.pages.messenger.ui.state_layouts.EmptyMessengerScreen
import com.soundhub.presentation.shared.containers.FetchStatusContainer
import com.soundhub.presentation.viewmodels.UiStateDispatcher
import kotlinx.coroutines.flow.Flow

@Composable
internal fun MessengerChatList(
	modifier: Modifier = Modifier,
	navController: NavHostController,
	uiStateDispatcher: UiStateDispatcher,
	messengerViewModel: MessengerViewModel
) {
	val messengerUiState: MessengerUiState by messengerViewModel
		.messengerUiState
		.collectAsState()

	val uiState: UiState by uiStateDispatcher.uiState.collectAsState(initial = UiState())
	val authorizedUser: User? = uiState.authorizedUser

	val chats: List<Chat> = messengerUiState.chats
	val searchBarText: String = uiState.searchBarText
	var filteredChats: List<Chat> by rememberSaveable { mutableStateOf(chats) }
	val messageChannel: Flow<Message> = uiStateDispatcher.receivedMessages
	val fetchStatus: ApiStatus = messengerUiState.status

	LaunchedEffect(key1 = true) {
		messengerViewModel.loadChats()
	}

	LaunchedEffect(key1 = messageChannel) {
		messageChannel.collect {
			messengerViewModel.loadChats()
			messengerViewModel.updateUnreadMessageCount()
			filteredChats = chats
		}
	}

	LaunchedEffect(key1 = searchBarText, key2 = chats) {
		Log.d("MessengerChatList", "searchbar text: $searchBarText")
		Log.d("MessengerChatList", "chats: $chats")
		filteredChats = messengerViewModel.filterChats(
			chats = chats,
			searchBarText = searchBarText,
			authorizedUser = authorizedUser
		)
	}

	FetchStatusContainer(status = fetchStatus) {
		if (filteredChats.isEmpty())
			EmptyMessengerScreen(
				navController = navController,
				uiStateDispatcher = uiStateDispatcher
			)
		else LazyColumn(
			modifier = modifier
				.fillMaxWidth()
				.padding(vertical = 5.dp),
			verticalArrangement = Arrangement.spacedBy(5.dp)
		) {
			items(items = filteredChats, key = { it.id }) { chat ->
				ChatCard(
					chat = chat,
					uiStateDispatcher = uiStateDispatcher,
					messengerViewModel = messengerViewModel
				)
			}
		}
	}
}