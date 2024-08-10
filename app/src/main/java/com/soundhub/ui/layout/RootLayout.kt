package com.soundhub.ui.layout

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.soundhub.ui.navigation.NavigationHost
import com.soundhub.ui.pages.authentication.AuthenticationViewModel
import com.soundhub.ui.pages.authentication.registration.RegistrationViewModel
import com.soundhub.ui.pages.messenger.MessengerViewModel
import com.soundhub.ui.pages.music.MusicViewModel
import com.soundhub.ui.pages.notifications.NotificationViewModel
import com.soundhub.ui.shared.bars.bottom.BottomNavigationBar
import com.soundhub.ui.shared.bars.top.TopAppBarBuilder
import com.soundhub.ui.viewmodels.MainViewModel
import com.soundhub.ui.viewmodels.UiStateDispatcher
import com.soundhub.utils.constants.Constants

@Composable
fun HomeScreen(
	modifier: Modifier = Modifier,
	navController: NavHostController,
	authViewModel: AuthenticationViewModel,
	uiStateDispatcher: UiStateDispatcher,
	registrationViewModel: RegistrationViewModel,
	messengerViewModel: MessengerViewModel,
	notificationViewModel: NotificationViewModel,
	mainViewModel: MainViewModel = hiltViewModel(),
	musicViewModel: MusicViewModel
) {
	val navBackStackEntry: NavBackStackEntry? by navController.currentBackStackEntryAsState()
	val currentRoute: String? = navBackStackEntry?.destination?.route
	val topBarTitle: String? by mainViewModel.topBarTitle.collectAsState()

	Scaffold(
		modifier = modifier
			.fillMaxSize()
			.background(MaterialTheme.colorScheme.background),
		topBar = {
			TopAppBarBuilder(
				currentRoute = currentRoute,
				topBarTitle = topBarTitle,
				navController = navController,
				uiStateDispatcher = uiStateDispatcher,
				notificationViewModel = notificationViewModel
			)
		},
		bottomBar = {
			if (currentRoute in Constants.ROUTES_WITH_BOTTOM_BAR)
				BottomNavigationBar(
					navController = navController,
					messengerViewModel = messengerViewModel,
					uiStateDispatcher = uiStateDispatcher
				)
		}
	) {
		NavigationHost(
			padding = it,
			navController = navController,
			authViewModel = authViewModel,
			registrationViewModel = registrationViewModel,
			uiStateDispatcher = uiStateDispatcher,
			messengerViewModel = messengerViewModel,
			notificationViewModel = notificationViewModel,
			mainViewModel = mainViewModel,
			musicViewModel = musicViewModel
		)
	}
}