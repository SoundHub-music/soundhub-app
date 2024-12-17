package com.soundhub.presentation.shared.bars.bottom.navigation.components

import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import com.soundhub.Route
import com.soundhub.presentation.pages.messenger.MessengerViewModel
import com.soundhub.presentation.shared.bars.bottom.navigation.model.NavBarMenuItem
import com.soundhub.presentation.viewmodels.UiStateDispatcher
import kotlinx.coroutines.flow.merge

@Composable
internal fun NavBarItemBadgeBox(
	messengerViewModel: MessengerViewModel,
	uiStateDispatcher: UiStateDispatcher,
	menuItem: NavBarMenuItem,
) {
	val receivedMessageChannel = uiStateDispatcher.receivedMessages
	val readMessagesChannel = uiStateDispatcher.receivedMessages
	var unreadMessageCount by rememberSaveable { mutableIntStateOf(0) }

	LaunchedEffect(key1 = receivedMessageChannel, key2 = readMessagesChannel) {
		unreadMessageCount = messengerViewModel.getUnreadChatCount()
		merge(receivedMessageChannel, readMessagesChannel).collect { _ ->
			unreadMessageCount = messengerViewModel.getUnreadChatCount()
		}
	}

	BadgedBox(
		badge = {
			if (unreadMessageCount > 0 && menuItem.route == Route.Messenger.route)
				Badge { Text(text = unreadMessageCount.toString()) }
		}
	) { NavBarItemIcon(menuItem.icon, contentDescription = menuItem.route) }
}