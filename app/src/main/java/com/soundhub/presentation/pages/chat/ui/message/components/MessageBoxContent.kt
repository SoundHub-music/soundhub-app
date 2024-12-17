package com.soundhub.presentation.pages.chat.ui.message.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.soundhub.domain.model.Message
import com.soundhub.presentation.pages.chat.ChatViewModel
import com.soundhub.presentation.pages.chat.ui.message.EmptyMessageParameters
import com.soundhub.presentation.pages.chat.ui.message.MessageParameters

@Composable
internal fun MessageBoxContent(
	message: Message,
	replyToMessage: Message? = null,
	isOwnMessage: Boolean = true,
	messageParameters: MessageParameters = EmptyMessageParameters,
	chatViewModel: ChatViewModel
) {
	val textRowPaddingTop = if (replyToMessage != null) 10.dp else 0.dp

	Box(
		modifier = messageParameters
			.boxModifier
			.padding(10.dp)
			.width(IntrinsicSize.Max)
	) {
		Column(horizontalAlignment = Alignment.End) {
			OriginMessage(
				replyToMessage = replyToMessage,
				messageParameters = messageParameters,
				chatViewModel = chatViewModel
			)

			Row(
				modifier = Modifier
					.fillMaxWidth()
					.padding(top = textRowPaddingTop),
				horizontalArrangement = Arrangement.spacedBy(10.dp),
				verticalAlignment = Alignment.Bottom
			) {
				Text(
					text = message.content,
					textAlign = TextAlign.Start,
					fontSize = 18.sp,
					letterSpacing = 0.5.sp,
					lineHeight = 24.sp,
					fontWeight = FontWeight.Normal,
					color = messageParameters.contentColor,
				)
			}

			MessageTimeAndMarkerRow(
				message = message,
				contentColor = messageParameters.contentColor,
				isOwnMessage = isOwnMessage
			)
		}
	}
}