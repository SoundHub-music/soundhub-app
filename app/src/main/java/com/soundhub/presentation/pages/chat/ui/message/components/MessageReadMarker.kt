package com.soundhub.presentation.pages.chat.ui.message.components

import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Done
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.soundhub.R
import com.soundhub.domain.model.Message

@Composable
internal fun MessageReadMarker(isOwnMessage: Boolean, message: Message) {
	if (isOwnMessage) {
		if (message.isRead)
			Icon(
				painter = painterResource(id = R.drawable.baseline_done_all_24),
				contentDescription = "message is read",
				tint = MaterialTheme.colorScheme.primary,
				modifier = Modifier.size(18.dp)
			)
		else Icon(
			imageVector = Icons.Rounded.Done,
			contentDescription = "message is sent",
			tint = MaterialTheme.colorScheme.primary,
			modifier = Modifier.size(18.dp)
		)
	}
}