package com.soundhub.ui.pages.messenger.components.chat_card

import android.net.Uri
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import com.soundhub.domain.model.User
import com.soundhub.ui.shared.avatar.CircularAvatar

@Composable
internal fun ChatCardAvatar(
	unreadMessageCount: Int,
	interlocutor: User?,
) {
	val interlocutorAvatarUrl: Uri? = remember(interlocutor) {
		interlocutor?.avatarUrl?.toUri()
	}

	BadgedBox(
		badge = {
			if (unreadMessageCount > 0) {
				Badge(modifier = Modifier.offset(x = (-35).dp)) {
					Text(text = unreadMessageCount.toString())
				}
			}
		}
	) {
		CircularAvatar(
			imageUri = interlocutorAvatarUrl,
			modifier = Modifier.size(40.dp)
		)
	}
}
