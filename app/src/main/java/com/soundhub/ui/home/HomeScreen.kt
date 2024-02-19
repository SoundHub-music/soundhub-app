package com.soundhub.ui.home

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import com.soundhub.Route
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
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
import com.soundhub.ui.create_post.CreatePostScreen
import com.soundhub.ui.friends.FriendListScreen
import com.soundhub.ui.gallery.GalleryScreen
import com.soundhub.ui.messenger_chat.MessengerChatScreen
import com.soundhub.ui.notifications.NotificationScreen
import com.soundhub.ui.settings.SettingsScreen
import com.soundhub.utils.Constants
import java.util.UUID


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

    LaunchedEffect(currentRoute) {
        Log.d("current_route", currentRoute.toString())
    }

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
                BottomNavigationBar(navController = navController, userCreds = userCreds.value)
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
                when (nestedRoute) {
                    Route.Authentication.ChooseGenres.route -> ChooseGenresScreen(
                        authViewModel = authViewModel,
                        navController = navController
                    )

                    Route.Authentication.ChooseArtists.route -> ChooseArtistsScreen(
                        authViewModel = authViewModel,
                        navController = navController
                    )

                    Route.Authentication.FillUserData.route -> FillUserDataScreen(
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
                route = Route.Messenger.Chat().route,
                arguments = listOf(navArgument(Constants.CHAT_NAV_ARG) {NavType.StringType})
            ) { entry ->
                val chatId = entry.arguments?.getString(Constants.CHAT_NAV_ARG)
                topBarTitle = "User"
                MessengerChatScreen(chatId = chatId)
            }

            composable(
                route = Route.Profile().route,
                arguments = listOf(navArgument(Constants.PROFILE_NAV_ARG) { NavType.StringType })
            ) { entry ->
                val context = LocalContext.current
                runCatching {
                    val userId = UUID.fromString(entry.arguments?.getString(Constants.PROFILE_NAV_ARG))
                    ProfileScreen(navController = navController, userId = userId)
                }
                    .onFailure {
                        Toast.makeText(
                            context,
                            stringResource(id = R.string.toast_user_profile_error),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
            }

            composable(Route.FriendList.route) {
                if (userCreds.value?.id != null) {
                    topBarTitle = stringResource(id = R.string.screen_title_friends)
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

            composable(Route.Settings.route) {
                if (userCreds.value?.id != null) {
                    topBarTitle = stringResource(id = R.string.screen_title_settings)
                    SettingsScreen()
                }
            }

            composable(
                route = "${Route.Gallery.route}/{${Constants.GALLERY_NAV_ARG}}",
                arguments = listOf(navArgument(Constants.GALLERY_NAV_ARG) {NavType.StringType})
            ) { entry ->
                topBarTitle = null
                val images = uiStateDispatcher.uiState.collectAsState().value.galleryUrls
                val initialPage = entry.arguments?.getString(Constants.GALLERY_NAV_ARG)?.toInt() ?: 0
                GalleryScreen(images = images, initialPage = initialPage)
            }

            composable(Route.CreatePost.route) {
                topBarTitle = stringResource(id = R.string.screen_title_create_post)
                CreatePostScreen()
            }
        }
    }
}
