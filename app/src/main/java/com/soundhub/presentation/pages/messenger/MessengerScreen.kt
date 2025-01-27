package com.soundhub.presentation.pages.messenger

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.navigation.NavHostController
import com.soundhub.presentation.pages.messenger.ui.containers.MessengerChatList
import com.soundhub.presentation.shared.containers.ContentContainer
import com.soundhub.presentation.viewmodels.UiStateDispatcher
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MessengerScreen(
	navController: NavHostController,
	uiStateDispatcher: UiStateDispatcher,
	messengerViewModel: MessengerViewModel
) {
	var isRefreshing: Boolean by remember { mutableStateOf(false) }
	val refreshState = rememberPullToRefreshState()
	val coroutineScope = rememberCoroutineScope()

	PullToRefreshBox(
		isRefreshing = isRefreshing,
		state = refreshState,
		onRefresh = {
			isRefreshing = true

			coroutineScope.launch {
				messengerViewModel.loadChats()
				isRefreshing = false
			}
		}
	) {
		ContentContainer {
			MessengerChatList(
				navController = navController,
				uiStateDispatcher = uiStateDispatcher,
				messengerViewModel = messengerViewModel
			)
		}
	}
}