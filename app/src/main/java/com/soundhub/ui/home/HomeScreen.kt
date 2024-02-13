package com.soundhub.ui.home

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
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
import com.soundhub.UiStateDispatcher
import com.soundhub.data.datastore.UserPreferences
import com.soundhub.ui.authentication.postregistration.ChooseGenresScreen
import com.soundhub.ui.authentication.postregistration.ChooseArtistsScreen
import com.soundhub.ui.authentication.postregistration.FillUserDataScreen
import com.soundhub.ui.components.bars.bottom.BottomNavigationBar
import com.soundhub.ui.edit_profile.EditUserProfileScreen
import com.soundhub.ui.components.bars.top.TopAppBarBuilder
import com.soundhub.ui.friends.FriendListScreen
import com.soundhub.ui.gallery.GalleryScreen
import com.soundhub.ui.messenger_chat.MessengerChatScreen
import com.soundhub.ui.notifications.NotificationScreen
import com.soundhub.ui.settings.SettingsScreen
import com.soundhub.utils.Constants


@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    authViewModel: AuthenticationViewModel = hiltViewModel(),
    uiStateDispatcher: UiStateDispatcher = hiltViewModel()
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
                uiStateDispatcher = uiStateDispatcher
            )
        },
        bottomBar = {
            if (currentRoute in Constants.ROUTES_WITH_BOTTOM_BAR)
                BottomNavigationBar(navController)
        }
    ) {
        NavHost(
            modifier = Modifier.padding(it),
            navController = navController,
            startDestination = if (userCreds.value?.id == null)
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
                if (userCreds.value?.id != null) {
                    topBarTitle = stringResource(id = R.string.screen_title_postline)
                    PostLineScreen(navController = navController)
                }
            }

            composable(Route.Music.route) {
                if (userCreds.value?.id != null) {
                    topBarTitle = stringResource(id = R.string.screen_title_music)
                    MusicScreen()
                }
            }

            composable(Route.Messenger.route) {
                if (userCreds.value?.id != null) {
                    topBarTitle = stringResource(id = R.string.screen_title_messenger)
                    MessengerScreen(navController)
                }
            }

            composable(
                route = Route.Messenger.Chat("{${Constants.CHAT_NAV_ARG}}").route,
                arguments = listOf(navArgument(Constants.CHAT_NAV_ARG) {NavType.StringType})
            ) { entry ->
                val chatId = entry.arguments?.getString(Constants.CHAT_NAV_ARG)
                topBarTitle = "User"
                MessengerChatScreen(chatId = chatId)
            }

            composable(Route.Profile.route) {
                if (userCreds.value?.id != null) {
                    topBarTitle = null
                    ProfileScreen(
                        authViewModel = authViewModel,
                        navController = navController,
                        userCreds = userCreds.value
                    )
                }
            }

            composable(Route.FriendList.route) {
                if (userCreds.value?.id != null) {
                    topBarTitle = "Друзья"
                    FriendListScreen()
                }
            }

            composable(Route.Notifications.route) {
                if (userCreds.value?.id != null) {
                    topBarTitle = stringResource(id = R.string.screen_title_notifications)
                    NotificationScreen(navController)
                }
            }

            composable(Route.EditUserData.route) {
                if (userCreds.value?.id != null) {
                    topBarTitle = stringResource(id = R.string.screen_title_edit_profile)
                    EditUserProfileScreen(authViewModel = authViewModel)
                }
            }

            composable(
                route = "${Route.Profile.route}/{${Constants.PROFILE_NAV_ARG}}",
                arguments = listOf(navArgument(Constants.PROFILE_NAV_ARG) { NavType.StringType })
            ) {
                Text(text = "not implemented")
                // TODO: profile routing
            }

            composable(Route.Settings.route) {
                if (userCreds.value?.id != null) {
                    topBarTitle = stringResource(id = R.string.screen_title_settings)
                    SettingsScreen()
                }
            }

            composable(Route.Gallery.route) {
                topBarTitle = null
                val images = uiStateDispatcher.uiState.collectAsState().value.galleryUrls
                GalleryScreen(images = images)
            }
        }
    }
}
