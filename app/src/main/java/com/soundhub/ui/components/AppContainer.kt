package com.soundhub.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AccountCircle
import androidx.compose.material.icons.rounded.Email
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import com.soundhub.R
import com.soundhub.ui.authentication.AuthenticationViewModel
import com.soundhub.ui.authentication.AuthenticationScreen
import com.soundhub.ui.components.icons.queueMusicIconRounded
import com.soundhub.ui.messenger.MessengerScreen
import com.soundhub.ui.music.MusicScreen
import com.soundhub.ui.postline.PostLineScreen
import com.soundhub.ui.profile.ProfileScreen
import com.soundhub.utils.Routes
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.navigation.NavHostController
import com.soundhub.UiEventDispatcher
import com.soundhub.data.UserPreferences


@Composable
fun AppContainer(
    navController: NavHostController,
    uiEventDispatcher: UiEventDispatcher,
    modifier: Modifier = Modifier
) {
    val authViewModel: AuthenticationViewModel = hiltViewModel()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val userCreds: State<UserPreferences?> = authViewModel.userCreds.collectAsState(initial = null)
    var topBarTitle: String? by remember {
        mutableStateOf(null)
    }

    Scaffold(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        bottomBar = {
            if (currentRoute != Routes.Authentication.route)
                BottomNavigationBar(items = getNavBarItems(), navController = navController)
        },
        topBar = {
            if (topBarTitle != null)
                AppHeader(topBarTitle, Modifier.padding(0.dp))
        }
    ) {
        NavHost(
            modifier = Modifier.padding(it),
            navController = navController,
            startDestination = Routes.Authentication.route
        ) {
            composable(Routes.Authentication.route) {
                AuthenticationScreen()
            }

            composable(Routes.Postline.route) {
                topBarTitle = stringResource(id = R.string.screen_title_postline)
                PostLineScreen()
            }

            composable(Routes.Music.route) {
                topBarTitle = stringResource(id = R.string.screen_title_music)
                MusicScreen()
            }

            composable(Routes.Messenger.route) {
                topBarTitle = stringResource(id = R.string.screen_title_messenger)
                MessengerScreen()
            }

            composable(Routes.Profile.route) {
                topBarTitle = null
                ProfileScreen()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
fun getNavBarItems(): MutableList<NavigationItemApp> {
   return mutableListOf(
        NavigationItemApp(
            route = Routes.Postline,
            icon = { Icon(Icons.Rounded.Home, contentDescription = Routes.Postline.pageName) },
        ),
        NavigationItemApp(
            route = Routes.Music,
            icon = { Icon(queueMusicIconRounded(), contentDescription = Routes.Music.pageName) },
        ),
        NavigationItemApp(
            route = Routes.Messenger,
            icon = {
                BadgedBox(badge = { Badge { Text(text = "5")} }) {
                    Icon(Icons.Rounded.Email, contentDescription = Routes.Messenger.pageName)
                }
            },
        ),
        NavigationItemApp(
            route = Routes.Profile,
            icon = { Icon(Icons.Rounded.AccountCircle, contentDescription = Routes.Profile.pageName) }
        )
    )
}