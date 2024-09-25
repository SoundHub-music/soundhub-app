package com.soundhub.ui.pages.chat.components.message_box

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.soundhub.data.model.Message
import com.soundhub.ui.pages.chat.ChatViewModel
import kotlinx.coroutines.launch

@Composable
internal fun OriginMessage(
	replyToMessage: Message?,
	chatViewModel: ChatViewModel,
	messageParameters: MessageParameters = EmptyMessageParameters,
) {
	val coroutineScope = rememberCoroutineScope()

	replyToMessage?.let { message ->
		Row(
			modifier = Modifier
				.fillMaxWidth()
				.height(IntrinsicSize.Min)
				.clickable {
					coroutineScope.launch {
						chatViewModel.scrollToMessageById(messageId = replyToMessage.id)
					}
				},
			horizontalArrangement = Arrangement.spacedBy(15.dp),
			verticalAlignment = Alignment.CenterVertically,
		) {
			VerticalDivider(
				thickness = 3.dp,
				color = Color(0xFF6741EE),
				modifier = Modifier.clip(RoundedCornerShape(10.dp))
			)
			Column {
				Text(
					text = message.sender?.getFullName() ?: "",
					fontSize = 16.sp,
					color = messageParameters.contentColor
				)
				Text(
					text = message.content,
					textAlign = TextAlign.Justify,
					overflow = TextOverflow.Ellipsis,
					fontSize = 18.sp,
					letterSpacing = 0.5.sp,
					lineHeight = 24.sp,
					fontWeight = FontWeight.Normal,
					color = messageParameters.contentColor,
					modifier = Modifier.fillMaxWidth()
				)
			}
		}
	}
}