package com.soundhub.ui.pages.chat.components.input_box

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.soundhub.data.states.ChatUiState
import com.soundhub.ui.pages.chat.ChatViewModel

@Composable
fun MessageInputBox(
	modifier: Modifier = Modifier,
	lazyListState: LazyListState,
	chatViewModel: ChatViewModel,
) {
	val chatUiState: ChatUiState by chatViewModel.chatUiState.collectAsState()
	val messageCount: Int = chatUiState.chat?.messages?.size ?: 0

	Row(
		modifier = modifier
			.fillMaxWidth()
			.background(
				color = MaterialTheme.colorScheme.surfaceContainer,
				shape = RoundedCornerShape(16.dp)
			),
		horizontalArrangement = Arrangement.spacedBy(5.dp),
		verticalAlignment = Alignment.CenterVertically
	) {
		AttachFileButton()
		MessageTextField(
			chatViewModel = chatViewModel,
			modifier = Modifier.weight(1f)
		)

		Row {
			EmojiButton()
			SendMessageButton(
				messageCount = messageCount,
				lazyListState = lazyListState,
				chatViewModel = chatViewModel
			)
		}
	}
}