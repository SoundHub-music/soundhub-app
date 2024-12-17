package com.soundhub.presentation.pages.messenger.ui.cards.chat_card.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.soundhub.domain.model.Chat
import com.soundhub.domain.model.User
import com.soundhub.domain.states.UiState
import com.soundhub.presentation.pages.messenger.MessengerViewModel
import com.soundhub.presentation.viewmodels.UiStateDispatcher

@Composable
internal fun ChatCardContainer(
	messengerViewModel: MessengerViewModel,
	uiStateDispatcher: UiStateDispatcher,
	chat: Chat
) {
	val uiState: UiState by uiStateDispatcher.uiState.collectAsState(initial = UiState())
	val authorizedUser: User? = uiState.authorizedUser
	var interlocutor: User? by remember { mutableStateOf(null) }
	var unreadMessageCount: Int by remember { mutableIntStateOf(0) }
	val messageChannel = uiStateDispatcher.receivedMessages

	LaunchedEffect(key1 = authorizedUser, key2 = chat) {
		authorizedUser?.let {
			interlocutor = chat.participants.find { it.id != authorizedUser.id }
		}
	}

	LaunchedEffect(key1 = messageChannel, key2 = chat) {
		messageChannel.collect { message ->
			if (message.chatId == chat.id)
				unreadMessageCount = messengerViewModel.getUnreadMessageCountByChatId(chat.id)
		}
	}

	Row(
		modifier = Modifier.padding(16.dp),
		horizontalArrangement = Arrangement.spacedBy(16.dp)
	) {
		ChatCardAvatar(
			unreadMessageCount = unreadMessageCount,
			interlocutor = interlocutor,
		)

		ChatCardDetails(
			unreadMessageCount = unreadMessageCount,
			interlocutor = interlocutor,
			messengerViewModel = messengerViewModel,
			uiStateDispatcher = uiStateDispatcher,
			chat = chat
		)
	}
}