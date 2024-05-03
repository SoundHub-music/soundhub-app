package com.soundhub.ui.friends.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.soundhub.R
import com.soundhub.data.enums.ApiStatus
import com.soundhub.data.model.User
import com.soundhub.ui.components.containers.ContentContainer
import com.soundhub.ui.components.loaders.CircleLoader
import com.soundhub.ui.friends.EmptyFriendsScreen
import com.soundhub.ui.friends.FriendsUiState
import com.soundhub.ui.friends.FriendsViewModel
import com.soundhub.ui.friends.enums.FriendListPage

@OptIn(ExperimentalFoundationApi::class)
@Composable
internal fun FriendsScreenPager(
    selectedTabState: PagerState,
    tabs: List<FriendListPage>,
    navController: NavHostController,
    friendsUiState: FriendsUiState,
    filteredFriendList: List<User>,
    friendsViewModel: FriendsViewModel
) {
    ContentContainer {
        HorizontalPager(
            state = selectedTabState,
            modifier = Modifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically
        ) { page ->
            when (tabs[page]) {
                FriendListPage.MAIN -> {
                    if (filteredFriendList.isEmpty()) {
                        EmptyFriendsScreen(
                            selectedTabState = selectedTabState,
                            tabs = tabs
                        )
                    } else UserFriendsPage(
                        friendList = filteredFriendList,
                        navController = navController,
                        chosenPage = tabs[page],
                        friendsViewModel = friendsViewModel
                    )
                }
                FriendListPage.RECOMMENDATIONS -> {
                    when (friendsUiState.status) {
                        ApiStatus.LOADING -> Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center
                        ) { CircleLoader(modifier = Modifier.size(64.dp)) }
                        ApiStatus.ERROR -> {
                            Text(
                                text = stringResource(id = R.string.friends_recommended_friends_error_message),
                                textAlign = TextAlign.Center,
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        ApiStatus.SUCCESS -> {
                            UserFriendsPage(
                                friendList = friendsUiState.recommendedFriends,
                                navController = navController,
                                chosenPage = tabs[page],
                                friendsViewModel = friendsViewModel
                            )
                        }
                    }
                }
            }
        }
    }
}