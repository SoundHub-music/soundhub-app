package com.soundhub.presentation.pages.chat.ui.message

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.EaseInOutQuart
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
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
import com.soundhub.domain.model.Message
import com.soundhub.domain.states.ChatUiState
import com.soundhub.presentation.pages.chat.ChatViewModel
import com.soundhub.presentation.pages.chat.ui.message.components.MessageBoxContent
import com.soundhub.presentation.pages.chat.ui.message.components.MessageCheckbox
import com.soundhub.utils.constants.Constants
import java.util.UUID

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
internal fun MessageBox(
	modifier: Modifier = Modifier,
	message: Message,
	isOwnMessage: Boolean,
	chatViewModel: ChatViewModel,
	lazyListState: LazyListState
) {
	val contentAlignment by remember(isOwnMessage) {
		derivedStateOf {
			if (isOwnMessage) Alignment.CenterEnd
			else Alignment.CenterStart
		}
	}

	val highlightedMessageId: UUID? by chatViewModel.highlightedMessageId.collectAsState()
	val chatUiState: ChatUiState by chatViewModel.chatUiState.collectAsState()

	val isCheckMessagesModeEnabled: Boolean = chatUiState.isCheckMessageModeEnabled
	val checkedMessages: Set<Message> = chatUiState.checkedMessages
	var replyToMessage: Message? by remember { mutableStateOf(null) }

	var isHighlighted = remember(highlightedMessageId) {
		derivedStateOf { message.id == highlightedMessageId }
	}

	var bgColor =
		animateColorAsState(
			targetValue = if (isHighlighted.value) MaterialTheme.colorScheme
				.tertiaryContainer.copy(alpha = 0.7f)
			else Color.Transparent,
			label = "highlightedMessageColor",
			animationSpec = tween(
				durationMillis = Constants.HIGHLIGHT_MESSAGE_TIMEOUT,
				easing = EaseInOutQuart
			)
		)

	val messageParameters = getMessageParameters(isOwnMessage, modifier)

	LaunchedEffect(message) {
		if (message.replyToMessageId != null) {
			replyToMessage = chatViewModel.getMessageById(message.replyToMessageId)
		}
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
			}
			.background(
				color = bgColor.value,
				shape = RoundedCornerShape(5.dp)
			)
			.padding(vertical = 5.dp),
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
				lazyListState = lazyListState
			)
		}
	}
}

@Composable
private fun getMessageParameters(
	isOwnMessage: Boolean,
	modifier: Modifier = Modifier
): MessageParameters {
	return object : MessageParameters {
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
}