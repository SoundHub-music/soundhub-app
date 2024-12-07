package com.soundhub.ui.pages.chat.components.message_box

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.soundhub.domain.model.Message
import com.soundhub.utils.lib.DateFormatter

@Composable
internal fun MessageTimeAndMarkerRow(
	message: Message,
	contentColor: Color,
	isOwnMessage: Boolean
) {
	Row(
		verticalAlignment = Alignment.CenterVertically,
		horizontalArrangement = Arrangement.spacedBy(2.dp),
	) {
		Text(
			modifier = Modifier.height(IntrinsicSize.Min),
			text = DateFormatter.getHourAndMinuteWithSeparator(message.createdAt),
			fontSize = 12.sp,
			fontWeight = FontWeight.Light,
			color = contentColor,
		)
		MessageReadMarker(isOwnMessage = isOwnMessage, message = message)
	}
}