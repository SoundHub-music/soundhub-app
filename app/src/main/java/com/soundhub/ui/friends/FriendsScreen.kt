package com.soundhub.ui.friends

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.pager.PagerState
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
import com.soundhub.ui.components.containers.ContentContainer
import com.soundhub.ui.friends.components.FriendsScreenPager
import com.soundhub.ui.friends.components.FriendsScreenTabs
import com.soundhub.ui.friends.enums.FriendListPage
import com.soundhub.ui.states.UiState
import com.soundhub.utils.SearchUtils

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun FriendsScreen(
    uiStateDispatcher: UiStateDispatcher,
    navController: NavHostController,
    authorizedUser: User?,
    friendsViewModel: FriendsViewModel = hiltViewModel()
) {
    val tabs: List<FriendListPage> = listOf(
        FriendListPage.MAIN,
        FriendListPage.RECOMMENDATIONS
    )

    val selectedTabState = rememberPagerState(
        initialPage = 0,
        pageCount = { tabs.size }
    )

    val friends: List<User> = authorizedUser?.friends ?: emptyList()
    //listOf(
//        User(
//            firstName = "Alexey",
//            lastName = "Zaycev",
//            avatarUrl = "https://img.freepik.com/free-psd/3d-illustration-human-avatar-profile_23-2150671122.jpg",
//            country = "Russia",
//            city = "Novosibirsk"
//        ),
//        User(
//            firstName = "Nikolay",
//            lastName = "Pupkin",
//            avatarUrl = "https://img.freepik.com/free-psd/3d-illustration-human-avatar-profile_23-2150671122.jpg",
//            country = "Russia",
//            city = "Novosibirsk"
//        ),
//        User(
//            firstName = "Alexey",
//            lastName = "Zaycev",
//            avatarUrl = "https://img.freepik.com/free-psd/3d-illustration-human-avatar-profile_23-2150671122.jpg",
//            country = "Russia",
//            city = "Novosibirsk"
//        )
//    )
    val uiState: UiState by uiStateDispatcher.uiState.collectAsState()
    val friendsUiState: FriendsUiState by friendsViewModel.friendsUiState.collectAsState()
    val searchBarText: String = uiState.searchBarText
    var filteredFriendList: List<User> by rememberSaveable { mutableStateOf(friends) }

    LaunchedEffect(key1 = searchBarText) {
        filteredFriendList = filterFriendsList(searchBarText, friends)
    }

    LaunchedEffect(key1 = selectedTabState.currentPage) {
        filteredFriendList = changeUserListWhenChangesTab(
            tabs = tabs,
            selectedTabState = selectedTabState,
            authorizedUser = authorizedUser,
            friendsUiState = friendsUiState,
            friendsViewModel = friendsViewModel
        )
    }

    ContentContainer {
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
               filteredFriendList = filteredFriendList
           )
        }
    }
}

private fun filterFriendsList(searchBarText: String, friends: List<User>): List<User> {
    return if (searchBarText.isNotEmpty())
        friends.filter{ SearchUtils.compareWithUsername(it, searchBarText) }
    else friends
}

@OptIn(ExperimentalFoundationApi::class)
private fun changeUserListWhenChangesTab(
    tabs: List<FriendListPage>,
    selectedTabState: PagerState,
    authorizedUser: User?,
    friendsUiState: FriendsUiState,
    friendsViewModel: FriendsViewModel
): List<User> {
    return when (tabs[selectedTabState.currentPage]) {
        FriendListPage.MAIN -> authorizedUser?.friends ?: emptyList()
        FriendListPage.RECOMMENDATIONS -> {
            if (friendsUiState.recommendedFriends.isEmpty())
                friendsViewModel.loadRecommendedFriends(authorizedUser?.id)
            friendsUiState.recommendedFriends
        }
    }
}