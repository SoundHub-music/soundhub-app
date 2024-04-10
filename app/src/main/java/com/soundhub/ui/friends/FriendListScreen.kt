package com.soundhub.ui.friends

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.soundhub.ui.viewmodels.UiStateDispatcher
import com.soundhub.data.model.User
import com.soundhub.ui.authentication.AuthenticationViewModel
import com.soundhub.ui.components.containers.ContentContainer
import com.soundhub.ui.friends.components.UserFriendsPage
import com.soundhub.ui.friends.enums.FriendListPage
import com.soundhub.utils.SearchUtils
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun FriendListScreen(
    uiStateDispatcher: UiStateDispatcher,
    authViewModel: AuthenticationViewModel,
    navController: NavHostController
) {
    val tabs = listOf(
        FriendListPage.MAIN,
        FriendListPage.RECOMMENDATIONS
    )

    val scope = rememberCoroutineScope()
    val selectedTabState = rememberPagerState(
        initialPage = 0,
        pageCount = { tabs.size }
    )

    val friends = listOf(
        User(
            firstName = "Alexey",
            lastName = "Zaycev",
            avatarUrl = "https://img.freepik.com/free-psd/3d-illustration-human-avatar-profile_23-2150671122.jpg",
            country = "Russia",
            city = "Novosibirsk"
        ),
        User(
            firstName = "Nikolay",
            lastName = "Pupkin",
            avatarUrl = "https://img.freepik.com/free-psd/3d-illustration-human-avatar-profile_23-2150671122.jpg",
            country = "Russia",
            city = "Novosibirsk"
        ),
        User(
            firstName = "Alexey",
            lastName = "Zaycev",
            avatarUrl = "https://img.freepik.com/free-psd/3d-illustration-human-avatar-profile_23-2150671122.jpg",
            country = "Russia",
            city = "Novosibirsk"
        )
    )
    val uiState by uiStateDispatcher.uiState.collectAsState()
    val searchBarText: String = uiState.searchBarText
    var filteredFriendList by rememberSaveable { mutableStateOf(friends) }

    LaunchedEffect(key1 = searchBarText) {
        filteredFriendList = if (searchBarText.isNotEmpty()) {
            friends.filter{ SearchUtils.compareWithUsername(it, searchBarText) }
        } else friends
    }

    ContentContainer {
        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            PrimaryTabRow(
                selectedTabIndex = selectedTabState.currentPage,
            ) {
                tabs.forEachIndexed { index, tab ->
                    Tab(
                        selected = index == selectedTabState.currentPage,
                        onClick = {
                            scope.launch { selectedTabState.animateScrollToPage(index) }
                        },
                        text = {
                            Text(
                                text = stringResource(id = tab.titleId),
                                fontWeight = FontWeight.Medium,
                                fontSize = 14.sp,
                                lineHeight = 20.sp,
                                fontFamily = FontFamily.SansSerif
                            )
                        }
                    )
                }
            }

            HorizontalPager(
                state = selectedTabState,
                modifier = Modifier.fillMaxSize()
            ) { page ->
                // TODO: implement dynamic friend list depending on the selected tab
                val userList = filteredFriendList

                UserFriendsPage(
                    friendList = userList,
                    navController = navController,
                    chosenPage = tabs[page]
                )

            }
        }
    }
}