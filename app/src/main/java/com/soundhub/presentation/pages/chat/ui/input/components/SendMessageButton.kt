package com.soundhub.presentation.pages.chat.ui.input.components

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.Send
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.soundhub.domain.states.ChatUiState
import com.soundhub.presentation.pages.chat.ChatViewModel

@Composable
internal fun SendMessageButton(chatViewModel: ChatViewModel, lazyListState: LazyListState) {
	val chatUiState: ChatUiState by chatViewModel.chatUiState.collectAsState()
	val messageContent: String = chatUiState.messageContent

	IconButton(
		enabled = messageContent.isNotEmpty(),
		onClick = { chatViewModel.onSendMessageClick(lazyListState) }
	) {
		Icon(
			imageVector = Icons.AutoMirrored.Rounded.Send,
			contentDescription = "send message",
			tint = MaterialTheme.colorScheme.primary
		)
	}
}