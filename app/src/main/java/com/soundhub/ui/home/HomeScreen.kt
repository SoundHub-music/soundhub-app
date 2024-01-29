package com.soundhub.ui.home

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AccountCircle
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Email
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
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
import com.soundhub.utils.Route
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.soundhub.UiEventDispatcher
import com.soundhub.data.datastore.UserPreferences
import com.soundhub.ui.authentication.postregistration.ChooseGenresScreen
import com.soundhub.ui.authentication.postregistration.ChooseArtistsScreen
import com.soundhub.ui.authentication.postregistration.FillUserDataScreen
import com.soundhub.ui.components.AppHeader
import com.soundhub.ui.components.BottomNavigationBar
import com.soundhub.ui.components.NavigationItemApp
import com.soundhub.ui.components.TopBarButton
import com.soundhub.ui.settings.SettingsScreen
import com.soundhub.utils.Constants
import com.soundhub.utils.UiEvent


@Composable
fun HomeScreen(
    navController: NavHostController,
    uiEventDispatcher: UiEventDispatcher,
    modifier: Modifier = Modifier
) {
    val authViewModel: AuthenticationViewModel = hiltViewModel()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val userCreds: State<UserPreferences?> = authViewModel.userCreds.collectAsState(initial = null)
    var topBarTitle: String? by remember { mutableStateOf(null) }

    Scaffold(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        topBar = {
            TopAppBarBuilder(
                currentRoute = currentRoute,
                topBarTitle = topBarTitle,
                uiEventDispatcher = uiEventDispatcher,
                topBarButton = {
                    TopBarButton(
                        currentRoute = currentRoute ?: "",
                        uiEventDispatcher = uiEventDispatcher
                    )
                }
            )
        },
        bottomBar = { NavigationBarBuilder(navController, currentRoute) }
    ) {
        NavHost(
            modifier = Modifier.padding(it),
            navController = navController,
            startDestination = if (userCreds.value == null)
                Route.Authentication.route else Route.Postline.route
        ) {
            composable(Route.Authentication.route) {
                topBarTitle = null
                AuthenticationScreen()
            }

            composable(
                route = "${Route.Authentication.route}/{${Constants.POST_REGISTER_NAV_ARG}}",
                arguments = listOf(navArgument(Constants.POST_REGISTER_NAV_ARG) {NavType.StringType})
            ) { entry ->
                val nestedRoute = "${Route.Authentication.route}/${entry.arguments?.getString(Constants.POST_REGISTER_NAV_ARG)}"

                Log.d("nested_auth_route", nestedRoute)
                when (Route.valueOf(nestedRoute)) {
                    is Route.Authentication.ChooseGenres -> ChooseGenresScreen(authViewModel)
                    is Route.Authentication.ChooseArtists -> ChooseArtistsScreen(authViewModel)
                    is Route.Authentication.FillUserData -> FillUserDataScreen(authViewModel = authViewModel)
                    else -> uiEventDispatcher.sendUiEvent(UiEvent.Navigate(Route.Authentication))
                }
            }

            composable(Route.Postline.route) {
                topBarTitle = stringResource(id = R.string.screen_title_postline)
                PostLineScreen()
            }

            composable(Route.Music.route) {
                topBarTitle = stringResource(id = R.string.screen_title_music)
                MusicScreen()
            }

            composable(Route.Messenger.route) {
                topBarTitle = stringResource(id = R.string.screen_title_messenger)
                MessengerScreen()
            }

            composable(Route.Profile.route) {
                topBarTitle = null
                ProfileScreen(userCreds.value?.id)
            }

            composable(
                route = "${Route.Profile.route}/{${Constants.PROFILE_NAV_ARG}}",
                arguments = listOf(navArgument(Constants.PROFILE_NAV_ARG) {NavType.StringType})
            ) {
                Text(text = "not implemented")
            }

            composable(Route.Settings.route) {
                topBarTitle = stringResource(id = R.string.screen_title_settings)
                SettingsScreen()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
private fun getNavBarItems(): List<NavigationItemApp> {
   return listOf(
        NavigationItemApp(
            route = Route.Postline,
            icon = { Icon(Icons.Rounded.Home, contentDescription = "Home") },
        ),
        NavigationItemApp(
            route = Route.Music,
            icon = { Icon(queueMusicIconRounded(), contentDescription = "Music") },
        ),
        NavigationItemApp(
            route = Route.Messenger,
            icon = {
                BadgedBox(badge = { Badge { Text(text = "5")} }) {
                    Icon(Icons.Rounded.Email, contentDescription = "Messenger")
                }
            },
        ),
        NavigationItemApp(
            route = Route.Profile,
            icon = { Icon(Icons.Rounded.AccountCircle, contentDescription = "Profile") }
        )
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TopAppBarBuilder(
    currentRoute: String?,
    topBarTitle: String?,
    uiEventDispatcher: UiEventDispatcher,
    topBarButton: @Composable () -> Unit = {}
) {
    if (currentRoute.equals(Route.Settings.route))
        TopAppBar(
            title = { Text(text = stringResource(id = R.string.screen_title_settings))},
            navigationIcon = {
                IconButton(
                    onClick = { uiEventDispatcher.sendUiEvent(UiEvent.Navigate(Route.Profile)) }
                ) {
                    Icon(
                        Icons.Rounded.ArrowBack,
                        contentDescription = stringResource(id = R.string.btn_description_back)
                    )
                }
            }
        )
    else topBarTitle?.let {
        AppHeader(
            modifier = Modifier.padding(0.dp),
            pageName = topBarTitle,
            actionButton = topBarButton
        )
    }
}

@Composable
private fun NavigationBarBuilder(navController: NavHostController, currentRoute: String?) {
    val allowedRoutes: List<String> = listOf(
        Route.Profile.route,
        Route.Postline.route,
        Route.Music.route,
        Route.Messenger.route
    )
    if (currentRoute in allowedRoutes)
        BottomNavigationBar(items = getNavBarItems(), navController = navController)
}