package com.soundhub.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavHostController
import com.soundhub.data.model.User
import com.soundhub.ui.authentication.AuthenticationViewModel
import com.soundhub.ui.viewmodels.UiStateDispatcher
import com.soundhub.ui.authentication.registration.RegistrationViewModel
import com.soundhub.ui.components.bars.bottom.BottomNavigationBar
import com.soundhub.ui.components.bars.top.TopAppBarBuilder
import com.soundhub.ui.edit_profile.profile.EditUserProfileViewModel
import com.soundhub.ui.messenger.MessengerViewModel
import com.soundhub.ui.navigation.NavigationHost
import com.soundhub.ui.notifications.NotificationViewModel
import com.soundhub.ui.states.UiState
import com.soundhub.utils.constants.Constants

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    authViewModel: AuthenticationViewModel,
    uiStateDispatcher: UiStateDispatcher,
    registrationViewModel: RegistrationViewModel,
    messengerViewModel: MessengerViewModel,
    editUserProfileViewModel: EditUserProfileViewModel,
    notificationViewModel: NotificationViewModel
) {
    val navBackStackEntry: NavBackStackEntry? by navController.currentBackStackEntryAsState()
    val currentRoute: String? = navBackStackEntry?.destination?.route
    val topBarTitle: MutableState<String?> = rememberSaveable { mutableStateOf(null) }
    val uiState: UiState by uiStateDispatcher.uiState.collectAsState()

    val authorizedUser: User? = uiState.authorizedUser


    Scaffold(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        topBar = {
            TopAppBarBuilder(
                currentRoute = currentRoute,
                topBarTitle = topBarTitle.value,
                navController = navController,
                uiStateDispatcher = uiStateDispatcher,
            )
        },
        bottomBar = {
            authorizedUser?.let { user ->
            if (currentRoute in Constants.ROUTES_WITH_BOTTOM_BAR)
                BottomNavigationBar(
                    navController = navController,
                    messengerViewModel = messengerViewModel,
                    user = user,
                    uiStateDispatcher = uiStateDispatcher
                )
            }
        }
    ) {
        NavigationHost(
            padding = it,
            navController = navController,
            authViewModel = authViewModel,
            registrationViewModel = registrationViewModel,
            uiStateDispatcher = uiStateDispatcher,
            messengerViewModel = messengerViewModel,
            editUserProfileViewModel = editUserProfileViewModel,
            notificationViewModel = notificationViewModel,
            topBarTitle = topBarTitle,
            authorizedUser = authorizedUser
        )
    }
}