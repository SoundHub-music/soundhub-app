package com.soundhub.ui.pages.chat.components.message_box

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import com.soundhub.data.model.Message
import com.soundhub.data.states.ChatUiState
import com.soundhub.ui.pages.chat.ChatViewModel

internal interface MessageParameters {
	val boxGradient: List<Color>
	val contentColor: Color
	val boxModifier: Modifier
}

internal object EmptyMessageParameters : MessageParameters {
	override val boxGradient = emptyList<Color>()
	override val contentColor = Color.Unspecified
	override val boxModifier = Modifier
}

@Composable
fun MessageBox(
	modifier: Modifier = Modifier,
	message: Message,
	isOwnMessage: Boolean,
	chatViewModel: ChatViewModel,
) {
	var contentAlignment by remember { mutableStateOf(Alignment.CenterEnd) }

	val chatUiState: ChatUiState by chatViewModel.chatUiState.collectAsState()
	val isCheckMessagesModeEnabled: Boolean = chatUiState.isCheckMessageModeEnabled
	val checkedMessages: List<Message> = chatUiState.checkedMessages
	var replyToMessage: Message? by remember { mutableStateOf(null) }

	LaunchedEffect(message) {
		if (message.replyToMessageId != null) {
			replyToMessage = chatViewModel.getMessageById(message.replyToMessageId)
		}
	}

	val messageParameters = object : MessageParameters {
		override val boxGradient = listOf(
			Color(0xFFD0BCFF),
			Color(0xFF966BF1)
		)
		override val contentColor = if (isOwnMessage)
			MaterialTheme.colorScheme.onSecondaryContainer
		else
			MaterialTheme.colorScheme.background

		override val boxModifier = if (isOwnMessage) modifier
			.background(
				color = MaterialTheme.colorScheme.secondaryContainer,
				shape = RoundedCornerShape(10.dp)
			)
		else modifier
			.background(
				brush = Brush.verticalGradient(boxGradient),
				shape = RoundedCornerShape(10.dp)
			)
	}

	LaunchedEffect(isOwnMessage) {
		contentAlignment = if (isOwnMessage)
			Alignment.CenterEnd
		else Alignment.CenterStart
	}

	Box(
		modifier = Modifier
			.fillMaxWidth()
			.pointerInput(isCheckMessagesModeEnabled, checkedMessages) {
				chatViewModel.onMessagePointerInputEvent(
					scope = this,
					checkedMessages = checkedMessages,
					isCheckMessagesMode = isCheckMessagesModeEnabled,
					message = message
				)
			},
		contentAlignment = contentAlignment
	) {
		MessageCheckbox(
			message = message,
			checkedMessages = checkedMessages,
			isCheckMessagesModeEnabled = isCheckMessagesModeEnabled
		) {
			MessageBoxContent(
				message = message,
				replyToMessage = replyToMessage,
				messageParameters = messageParameters,
				isOwnMessage = isOwnMessage,
				chatViewModel = chatViewModel,
			)
		}
	}
}
