package com.soundhub.ui.home

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
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
import com.soundhub.ui.messenger.MessengerScreen
import com.soundhub.ui.music.MusicScreen
import com.soundhub.ui.postline.PostLineScreen
import com.soundhub.ui.profile.ProfileScreen
import com.soundhub.utils.Route
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.soundhub.data.datastore.UserPreferences
import com.soundhub.ui.authentication.postregistration.ChooseGenresScreen
import com.soundhub.ui.authentication.postregistration.ChooseArtistsScreen
import com.soundhub.ui.authentication.postregistration.FillUserDataScreen
import com.soundhub.ui.components.AppHeader
import com.soundhub.ui.components.BottomNavigationBar
import com.soundhub.ui.components.top_bar.TopBarActions
import com.soundhub.ui.components.top_bar.TopBarButton
import com.soundhub.ui.edit_profile.EditUserProfileScreen
import com.soundhub.ui.notifications.NotificationScreen
import com.soundhub.ui.settings.SettingsScreen
import com.soundhub.utils.Constants


@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    authViewModel: AuthenticationViewModel = hiltViewModel(),
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val userCreds: State<UserPreferences?> = authViewModel.userCreds.collectAsState(initial = null)
    var topBarTitle: String? by rememberSaveable { mutableStateOf(null) }

    Scaffold(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        topBar = {
            TopAppBarBuilder(
                currentRoute = currentRoute,
                topBarTitle = topBarTitle,
                navController = navController,
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
                AuthenticationScreen(authViewModel)
            }

            composable(
                route = "${Route.Authentication.route}/{${Constants.POST_REGISTER_NAV_ARG}}",
                arguments = listOf(navArgument(Constants.POST_REGISTER_NAV_ARG) { NavType.StringType })
            ) { entry ->
                val nestedRoute =
                    "${Route.Authentication.route}/${entry.arguments?.getString(Constants.POST_REGISTER_NAV_ARG)}"

                Log.d("nested_auth_route", nestedRoute)
                when (Route.valueOf(nestedRoute)) {
                    is Route.Authentication.ChooseGenres -> ChooseGenresScreen(
                        authViewModel = authViewModel,
                        navController = navController
                    )

                    is Route.Authentication.ChooseArtists -> ChooseArtistsScreen(
                        authViewModel = authViewModel,
                        navController = navController
                    )

                    is Route.Authentication.FillUserData -> FillUserDataScreen(
                        authViewModel = authViewModel,
                        navController = navController
                    )

                    else -> navController.navigate(Route.Authentication.route)
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
                ProfileScreen(
                    authViewModel = authViewModel,
                    navController = navController,
                    userCreds = userCreds.value
                )
            }

            composable(Route.Notifications.route) {
                topBarTitle = stringResource(id = R.string.screen_title_notifications)
                NotificationScreen(navController)
            }

            composable(Route.EditUserData.route) {
                topBarTitle = stringResource(id = R.string.screen_title_edit_profile)
                EditUserProfileScreen()
            }

            composable(
                route = "${Route.Profile.route}/{${Constants.PROFILE_NAV_ARG}}",
                arguments = listOf(navArgument(Constants.PROFILE_NAV_ARG) { NavType.StringType })
            ) {
                Text(text = "not implemented")
                // TODO: profile routing
            }

            composable(Route.Settings.route) {
                topBarTitle = stringResource(id = R.string.screen_title_settings)
                SettingsScreen()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TopAppBarBuilder(
    currentRoute: String?,
    topBarTitle: String?,
    navController: NavHostController,
) {
    val topAppBarScreens: List<String> = listOf(
        Route.Settings.route,
        Route.Notifications.route,
        Route.EditUserData.route
    )

    if (topAppBarScreens.contains(currentRoute))
        TopAppBar(
            title = { Text(text = topBarTitle ?: "") },
            navigationIcon = {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(
                        imageVector = Icons.Rounded.ArrowBack,
                        contentDescription = stringResource(id = R.string.btn_description_back)
                    )
                }
            },
            actions = {
                TopBarActions(
                    currentRoute = currentRoute,
                    navController = navController
                )
            }
        )
    else topBarTitle?.let {
        AppHeader(
            modifier = Modifier.padding(0.dp),
            pageName = topBarTitle,
            actionButton = {
                TopBarButton(
                    currentRoute = currentRoute ?: "",
                    navController = navController
                )
            }
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
        BottomNavigationBar(navController)
}