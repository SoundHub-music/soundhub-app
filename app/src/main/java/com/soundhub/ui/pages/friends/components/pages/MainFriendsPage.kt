package com.soundhub.ui.pages.friends.components.pages

import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.pager.PagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.navigation.NavHostController
import com.soundhub.data.states.FriendsUiState
import com.soundhub.data.states.UiState
import com.soundhub.domain.model.User
import com.soundhub.ui.pages.friends.EmptyFriendsScreen
import com.soundhub.ui.pages.friends.FriendsViewModel
import com.soundhub.ui.pages.friends.UnauthorizedEmptyFriendsPage
import com.soundhub.ui.pages.friends.components.UserFriendsContainer
import com.soundhub.ui.viewmodels.UiStateDispatcher

@OptIn(ExperimentalFoundationApi::class)
@Composable
internal fun MainFriendsPage(
	friendsViewModel: FriendsViewModel,
	uiStateDispatcher: UiStateDispatcher,
	navController: NavHostController,
	selectedTabState: PagerState,
	page: Int
) {
	val uiState: UiState by uiStateDispatcher.uiState.collectAsState(initial = UiState())
	val friendsUiState: FriendsUiState by friendsViewModel.friendsUiState.collectAsState()
	val isOriginProfile = friendsViewModel.isOriginProfile()
	val tabs = friendsViewModel.getTabs()

	val profileOwner: User? = friendsUiState.profileOwner
	val searchBarText: String = uiState.searchBarText
	val friends: List<User> = profileOwner?.friends.orEmpty().distinct()
	var filteredFriendList: List<User> by rememberSaveable { mutableStateOf(friends) }

	LaunchedEffect(key1 = searchBarText) {
		Log.d("MainFriendsPage", "text: $searchBarText")
		filteredFriendList = friendsViewModel
			.filterFriendsList(searchBarText, friends)
	}

	LaunchedEffect(key1 = profileOwner, key2 = friends) {
		filteredFriendList = friends
	}


	if (friends.isEmpty()) {
		if (!isOriginProfile) {
			UnauthorizedEmptyFriendsPage()
		} else EmptyFriendsScreen(selectedTabState, friendsViewModel)
	} else UserFriendsContainer(
		friendList = filteredFriendList,
		navController = navController,
		chosenPage = tabs[page],
		friendsViewModel = friendsViewModel
	)
}