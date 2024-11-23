package com.soundhub.ui.pages.friends.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import com.soundhub.ui.pages.friends.FriendsViewModel
import com.soundhub.ui.pages.friends.components.pages.MainFriendsPage
import com.soundhub.ui.pages.friends.components.pages.SearchUserPage
import com.soundhub.ui.pages.friends.components.pages.UserRecommendationsPage
import com.soundhub.ui.pages.friends.enums.FriendListPage
import com.soundhub.ui.shared.containers.ContentContainer
import com.soundhub.ui.viewmodels.UiStateDispatcher

@OptIn(ExperimentalFoundationApi::class)
@Composable
internal fun FriendsScreenPager(
	selectedTabState: PagerState,
	navController: NavHostController,
	friendsViewModel: FriendsViewModel,
	uiStateDispatcher: UiStateDispatcher,
) {
	val tabs: List<FriendListPage> = friendsViewModel.getTabs()
	val isOriginProfile = friendsViewModel.isOriginProfile()

	ContentContainer {
		if (isOriginProfile) {
			HorizontalPager(
				state = selectedTabState,
				modifier = Modifier.fillMaxSize(),
				verticalAlignment = Alignment.CenterVertically
			) { page ->
				PagerPages(
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
			page = tabs.indexOf(FriendListPage.MAIN),
		)
	}
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun PagerPages(
	friendsViewModel: FriendsViewModel,
	uiStateDispatcher: UiStateDispatcher,
	selectedTabState: PagerState,
	navController: NavHostController,
	page: Int
) {
	val tabs: List<FriendListPage> = friendsViewModel.getTabs()
	if (page < tabs.size) {
		when (tabs[page]) {
			FriendListPage.MAIN -> MainFriendsPage(
				friendsViewModel = friendsViewModel,
				uiStateDispatcher = uiStateDispatcher,
				selectedTabState = selectedTabState,
				navController = navController,
				page = page,
			)

			FriendListPage.RECOMMENDATIONS -> UserRecommendationsPage(
				friendsViewModel = friendsViewModel,
				uiStateDispatcher = uiStateDispatcher,
				navController = navController,
				page = page
			)

			FriendListPage.SEARCH -> SearchUserPage(
				friendsViewModel = friendsViewModel,
				navController = navController
			)
		}
	}
}