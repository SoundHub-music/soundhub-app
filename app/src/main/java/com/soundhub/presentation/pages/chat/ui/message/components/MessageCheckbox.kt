package com.soundhub.presentation.pages.chat.ui.message.components

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.soundhub.domain.model.Message

@Composable
internal fun MessageCheckbox(
	message: Message,
	checkedMessages: Set<Message>,
	isCheckMessagesModeEnabled: Boolean,
	content: @Composable () -> Unit,
) {
	BadgedBox(badge = {
		if (isCheckMessagesModeEnabled && message in checkedMessages) {
			Badge(
				containerColor = MaterialTheme.colorScheme.errorContainer
			) {
				Icon(
					imageVector = Icons.Rounded.Check,
					contentDescription = "checked message",
					modifier = Modifier
						.clip(CircleShape)
						.padding(5.dp)
						.size(20.dp)
				)
			}
		}
	}) { content() }
}