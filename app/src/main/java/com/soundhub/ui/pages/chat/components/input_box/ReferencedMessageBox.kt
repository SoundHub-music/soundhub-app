package com.soundhub.ui.pages.chat.components.input_box

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.soundhub.data.model.Message
import com.soundhub.data.states.ChatUiState
import com.soundhub.ui.pages.chat.ChatViewModel

@Composable
internal fun ReferencedMessageBox(chatViewModel: ChatViewModel) {
	val chatUiState: ChatUiState by chatViewModel.chatUiState.collectAsState()
	val isReplyMessageModeEnabled = chatUiState.isReplyMessageModeEnabled
	val checkedMessages: List<Message> = chatUiState.checkedMessages
	val messageToReply: Message? = checkedMessages.firstOrNull()

	if (isReplyMessageModeEnabled && messageToReply != null)
		Row(
			modifier = Modifier
				.fillMaxWidth()
				.background(color = MaterialTheme.colorScheme.background),
			horizontalArrangement = Arrangement.SpaceBetween,
			verticalAlignment = Alignment.CenterVertically
		) {
			Box(modifier = Modifier.height(IntrinsicSize.Min)) {
				VerticalDivider(
					thickness = 2.dp,
					color = MaterialTheme.colorScheme.secondaryContainer
				)
				Column(
					modifier = Modifier.padding(10.dp),
					verticalArrangement = Arrangement.spacedBy(2.dp)
				) {
					Text(
						text = messageToReply.author?.getFullName() ?: "",
						fontWeight = FontWeight.Bold,
						fontSize = 16.sp
					)

					Text(
						text = messageToReply.content,
						modifier = Modifier.fillMaxWidth(0.8f),
						fontSize = 14.sp,
						color = MaterialTheme
							.colorScheme
							.onSecondaryContainer
							.copy(alpha = 0.8f),
						maxLines = 2,
						overflow = TextOverflow.Ellipsis
					)
				}
			}

			IconButton(
				onClick = chatViewModel::unsetReplyMessageMode
			) {
				Icon(
					imageVector = Icons.Rounded.Close,
					contentDescription = null
				)
			}
		}
}