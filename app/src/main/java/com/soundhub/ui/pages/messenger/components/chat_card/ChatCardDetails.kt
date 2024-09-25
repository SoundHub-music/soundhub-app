package com.soundhub.ui.pages.messenger.components.chat_card

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.soundhub.R
import com.soundhub.data.model.Chat
import com.soundhub.data.model.User
import com.soundhub.ui.pages.messenger.MessengerViewModel
import com.soundhub.ui.viewmodels.UiStateDispatcher

@Composable
internal fun ChatCardDetails(
	unreadMessageCount: Int,
	uiStateDispatcher: UiStateDispatcher,
	messengerViewModel: MessengerViewModel,
	interlocutor: User?,
	chat: Chat
) {
	val context = LocalContext.current
	val messageChannel = uiStateDispatcher.receivedMessages

	val interlocutorFullName = interlocutor?.getFullName().orEmpty()

	var lastMessageContent: String? by rememberSaveable { mutableStateOf(null) }
	val messagePreviewPrefix = context.getString(R.string.messenger_screen_last_message_prefix)

	val lastMessageModifier = if (unreadMessageCount > 0) {
		Modifier
			.background(MaterialTheme.colorScheme.primary, RoundedCornerShape(16.dp))
			.padding(vertical = 2.dp, horizontal = 6.dp)
	} else Modifier

	LaunchedEffect(key1 = chat.id) {
		messengerViewModel.getLastMessageByChatId(chat.id)?.let { lastMessage ->
			lastMessageContent = messengerViewModel.prepareMessagePreview(
				messagePreviewPrefix,
				lastMessage
			)
		}
	}

	LaunchedEffect(key1 = messageChannel) {
		messageChannel.collect { message ->
			if (message.chatId == chat.id) {
				lastMessageContent = messengerViewModel.prepareMessagePreview(
					messagePreviewPrefix,
					message
				)
			}
		}
	}

	Column {
		Text(
			text = interlocutorFullName,
			fontWeight = FontWeight.ExtraBold,
			fontSize = 16.sp,
			lineHeight = 24.sp
		)

		lastMessageContent?.let {
			Text(
				text = it,
				modifier = lastMessageModifier,
				letterSpacing = 0.25.sp,
				maxLines = 1,
				overflow = TextOverflow.Ellipsis,
				fontWeight = if (unreadMessageCount > 0) FontWeight.Bold else FontWeight.Light,
				fontSize = 14.sp,
				lineHeight = 20.sp,
				color = if (unreadMessageCount > 0) MaterialTheme.colorScheme.onTertiary
				else MaterialTheme.colorScheme.onPrimaryContainer
			)
		}
	}
}
