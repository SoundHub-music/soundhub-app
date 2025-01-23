package com.soundhub.presentation.pages.friends.ui.pager

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import com.soundhub.presentation.pages.friends.FriendsViewModel
import com.soundhub.presentation.pages.friends.enums.FriendListPage
import com.soundhub.presentation.pages.friends.ui.pager.pages.MainFriendsPage
import com.soundhub.presentation.pages.friends.ui.pager.pages.SearchUserPage
import com.soundhub.presentation.pages.friends.ui.pager.pages.UserRecommendationsPage
import com.soundhub.presentation.shared.containers.ContentContainer
import com.soundhub.presentation.viewmodels.UiStateDispatcher

@OptIn(ExperimentalFoundationApi::class)
@Composable
internal fun FriendsScreenPager(
	selectedTabState: PagerState,
	navController: NavHostController,
	friendsViewModel: FriendsViewModel,
	uiStateDispatcher: UiStateDispatcher,
) {
	val isOriginProfile = friendsViewModel.isOriginProfile()

	ContentContainer {
		if (isOriginProfile) {
			HorizontalPager(
				state = selectedTabState,
				modifier = Modifier.fillMaxSize(),
				verticalAlignment = Alignment.CenterVertically
			) { page ->
				ActiveFriendsPage(
					friendsViewModel = friendsViewModel,
					uiStateDispatcher = uiStateDispatcher,
					selectedTabState = selectedTabState,
					navController = navController,
					page = page,
				)
			}
		} else MainFriendsPage(
			friendsViewModel = friendsViewModel,
			uiStateDispatcher = uiStateDispatcher,
			selectedTabState = selectedTabState,
			navController = navController,
		)
	}
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun ActiveFriendsPage(
	friendsViewModel: FriendsViewModel,
	uiStateDispatcher: UiStateDispatcher,
	selectedTabState: PagerState,
	navController: NavHostController,
	page: Int
) {
	val tabs: List<FriendListPage> = FriendsViewModel.Tabs
	if (page < tabs.size) {
		when (tabs[page]) {
			FriendListPage.MAIN -> MainFriendsPage(
				friendsViewModel = friendsViewModel,
				uiStateDispatcher = uiStateDispatcher,
				selectedTabState = selectedTabState,
				navController = navController,
			)

			FriendListPage.RECOMMENDATIONS -> UserRecommendationsPage(
				friendsViewModel = friendsViewModel,
				uiStateDispatcher = uiStateDispatcher,
				navController = navController,
			)

			FriendListPage.SEARCH -> SearchUserPage(
				friendsViewModel = friendsViewModel,
				navController = navController
			)
		}
	}
}