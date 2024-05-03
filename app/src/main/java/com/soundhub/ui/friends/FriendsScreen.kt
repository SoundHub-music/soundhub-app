package com.soundhub.ui.friends

import android.util.Log
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
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.soundhub.ui.viewmodels.UiStateDispatcher
import com.soundhub.data.model.User
import com.soundhub.ui.friends.components.FriendsScreenPager
import com.soundhub.ui.friends.components.FriendsScreenTabs
import com.soundhub.ui.friends.enums.FriendListPage
import com.soundhub.ui.states.UiState

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun FriendsScreen(
    uiStateDispatcher: UiStateDispatcher,
    navController: NavHostController,
    authorizedUser: User?,
    friendsViewModel: FriendsViewModel = hiltViewModel()
) {
    val tabs = friendsViewModel.tabs
    val selectedTabState = rememberPagerState(
        initialPage = 0,
        pageCount = { tabs.size }
    )

    val friends: List<User> = authorizedUser?.friends ?: emptyList()
    val uiState: UiState by uiStateDispatcher.uiState.collectAsState()
    val friendsUiState: FriendsUiState by friendsViewModel.friendsUiState.collectAsState()
    val searchBarText: String = uiState.searchBarText
    var filteredFriendList: List<User> by rememberSaveable { mutableStateOf(friends) }

    LaunchedEffect(key1 = searchBarText) {
        filteredFriendList = friendsViewModel
            .filterFriendsList(searchBarText, friends)
    }

    LaunchedEffect(key1 = selectedTabState.currentPage) {
        filteredFriendList = when (tabs[selectedTabState.currentPage]) {
            FriendListPage.MAIN -> authorizedUser?.friends ?: emptyList()
            FriendListPage.RECOMMENDATIONS -> friendsUiState.recommendedFriends
        }
    }

    LaunchedEffect(key1 = filteredFriendList) {
        Log.d("FriendsScreen", "users: $friendsUiState")
    }

    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
       FriendsScreenTabs(
           selectedTabState = selectedTabState,
           tabs = tabs
       )

       FriendsScreenPager(
           selectedTabState = selectedTabState,
           tabs = tabs,
           navController = navController,
           friendsUiState = friendsUiState,
           filteredFriendList = filteredFriendList,
           friendsViewModel = friendsViewModel
       )
    }
}