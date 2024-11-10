package com.soundhub.ui.pages.friends

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.soundhub.data.states.UiState
import com.soundhub.ui.pages.friends.components.FriendsScreenPager
import com.soundhub.ui.pages.friends.components.FriendsScreenTabs
import com.soundhub.ui.pages.friends.enums.FriendListPage
import com.soundhub.ui.viewmodels.UiStateDispatcher
import java.util.UUID

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun FriendsScreen(
	uiStateDispatcher: UiStateDispatcher,
	navController: NavHostController,
	friendsViewModel: FriendsViewModel,
	userId: UUID?
) {
	val tabs = friendsViewModel.tabs
	val uiState: UiState by uiStateDispatcher.uiState.collectAsState(initial = UiState())
	val selectedTabState = rememberPagerState(initialPage = 0, pageCount = { tabs.size })
	val isOriginProfile = friendsViewModel.isOriginProfile()
	var tabState: List<FriendListPage> by rememberSaveable { mutableStateOf(friendsViewModel.tabs) }

	val searchBarText: String = uiState.searchBarText

	LaunchedEffect(key1 = userId) {
		userId?.let { friendsViewModel.loadProfileOwner(it) }
	}

	LaunchedEffect(key1 = searchBarText) {
		if (tabs[selectedTabState.currentPage] == FriendListPage.SEARCH)
			friendsViewModel.searchUsers(searchBarText)
	}

	LaunchedEffect(key1 = isOriginProfile) {
		tabState = if (!isOriginProfile)
			listOf(FriendListPage.MAIN)
		else friendsViewModel.tabs
	}

	LaunchedEffect(key1 = selectedTabState.currentPage) {
		uiStateDispatcher.setSearchBarActive(false)
	}

	Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
		if (isOriginProfile) FriendsScreenTabs(
			selectedTabState = selectedTabState,
			tabs = tabState
		)
		FriendsScreenPager(
			selectedTabState = selectedTabState,
			navController = navController,
			friendsViewModel = friendsViewModel,
			uiStateDispatcher = uiStateDispatcher,
		)
	}
}