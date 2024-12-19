package com.soundhub.presentation.pages.chat.ui.input

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
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
import com.soundhub.domain.states.ChatUiState
import com.soundhub.presentation.pages.chat.ChatViewModel
import com.soundhub.presentation.pages.chat.ui.input.components.AttachFileButton
import com.soundhub.presentation.pages.chat.ui.input.components.EmojiButton
import com.soundhub.presentation.pages.chat.ui.input.components.MessageTextField
import com.soundhub.presentation.pages.chat.ui.input.components.ReferencedMessageBox
import com.soundhub.presentation.pages.chat.ui.input.components.SendMessageButton

@Composable
internal fun MessageInputBox(
	modifier: Modifier = Modifier,
	lazyListState: LazyListState,
	chatViewModel: ChatViewModel,
) {
	val chatUiState: ChatUiState by chatViewModel.chatUiState.collectAsState()
	val isReplyMessageModeEnabled = chatUiState.isReplyMessageModeEnabled

	Column(modifier = Modifier.fillMaxWidth()) {
		ReferencedMessageBox(chatViewModel)
		Row(
			modifier = modifier
				.fillMaxWidth()
				.background(
					color = MaterialTheme.colorScheme.surfaceContainer,
					shape = if (isReplyMessageModeEnabled)
						RoundedCornerShape(
							bottomStart = 16.dp,
							bottomEnd = 16.dp
						)
					else RoundedCornerShape(16.dp)
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
				SendMessageButton(chatViewModel, lazyListState)
			}
		}
	}
}