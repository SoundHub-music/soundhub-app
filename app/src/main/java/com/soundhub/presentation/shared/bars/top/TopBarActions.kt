package com.soundhub.presentation.shared.bars.top

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.soundhub.Route
import com.soundhub.domain.states.UiState
import com.soundhub.presentation.pages.notifications.NotificationViewModel
import com.soundhub.presentation.pages.postline.ui.buttons.NotificationTopBarButton
import com.soundhub.presentation.shared.buttons.SearchButton
import com.soundhub.presentation.shared.fields.TransparentSearchTextField
import com.soundhub.presentation.viewmodels.UiStateDispatcher
import com.soundhub.utils.constants.Constants

@Composable
fun TopBarActions(
	navController: NavHostController,
	uiStateDispatcher: UiStateDispatcher,
	notificationViewModel: NotificationViewModel
) {
	val currentBackStackEntry by navController.currentBackStackEntryAsState()
	val currentRoute: String? = currentBackStackEntry?.destination?.route
	val uiState: UiState by uiStateDispatcher.uiState.collectAsState(initial = UiState())

	val isSearchBarActive: Boolean = uiState.isSearchBarActive
	val searchBarText: String = uiState.searchBarText

	when (currentRoute) {
		Route.PostLine.route -> NotificationTopBarButton(
			navController = navController,
			notificationViewModel = notificationViewModel
		)

		in Constants.ROUTES_WITH_SEARCH_BAR -> {
			if (isSearchBarActive)
				TransparentSearchTextField(
					value = searchBarText,
					onValueChange = uiStateDispatcher::updateSearchBarText,
					uiStateDispatcher = uiStateDispatcher
				)
			else SearchButton(uiStateDispatcher)
		}

		else -> {}
	}
}