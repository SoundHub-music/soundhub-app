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
import androidx.compose.runtime.MutableState
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.soundhub.UiStateDispatcher
import com.soundhub.data.datastore.UserPreferences
import com.soundhub.data.model.User
import com.soundhub.ui.authentication.postregistration.ChooseGenresScreen
import com.soundhub.ui.authentication.postregistration.ChooseArtistsScreen
import com.soundhub.ui.authentication.postregistration.FillUserDataScreen
import com.soundhub.ui.components.bars.bottom.BottomNavigationBar
import com.soundhub.ui.edit_profile.EditUserProfileScreen
import com.soundhub.ui.components.bars.top.TopAppBarBuilder
import com.soundhub.ui.create_post.CreatePostScreen
import com.soundhub.ui.friends.FriendListScreen
import com.soundhub.ui.gallery.GalleryScreen
import com.soundhub.ui.messenger.MessengerViewModel
import com.soundhub.ui.messenger.chat.ChatScreen
import com.soundhub.ui.messenger.chat.ChatViewModel
import com.soundhub.ui.notifications.NotificationScreen
import com.soundhub.ui.settings.SettingsScreen
import com.soundhub.utils.Constants
import com.soundhub.viewmodels.UserViewModel


@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    authViewModel: AuthenticationViewModel = hiltViewModel(),
    uiStateDispatcher: UiStateDispatcher = hiltViewModel(),
    chatViewModel: ChatViewModel = hiltViewModel(),
    messengerViewModel: MessengerViewModel = hiltViewModel()
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val authorizedUser: User? by authViewModel.userInstance.collectAsState(initial = null)
    val userCreds by authViewModel.userCreds.collectAsState(initial = null)
    var topBarTitle: String? by rememberSaveable { mutableStateOf(null) }

    val userViewModel: UserViewModel = hiltViewModel()

    LaunchedEffect(key1 = currentRoute) {
        Log.d("current_route", "HomeScreen: $currentRoute")
        Log.d("authorized_user", authorizedUser.toString())
        Log.d("user_creds", userCreds.toString())
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
                BottomNavigationBar(
                    navController = navController,
                    user = authorizedUser
                )
        }
    ) {
        NavHost(
            modifier = Modifier.padding(it),
            navController = navController,
            startDestination = if (userCreds?.accessToken != null)
                Route.Postline.route
            else Route.Authentication.route
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
                ScreenContainer(
                    userCreds = userCreds,
                    navController = navController
                ) {
                    topBarTitle = stringResource(id = R.string.screen_title_postline)
                    PostLineScreen(
                        navController = navController,
                        uiStateDispatcher = uiStateDispatcher
                    )
                }
            }

            composable(Route.Music.route) {
                ScreenContainer(
                    userCreds = userCreds,
                    navController = navController
                ) {
                    topBarTitle = stringResource(id = R.string.screen_title_music)
                    MusicScreen(navController = navController)
                }
            }

            composable(Route.Messenger.route) {
                ScreenContainer(
                    userCreds = userCreds,
                    navController = navController
                ) {
                    topBarTitle = stringResource(id = R.string.screen_title_messenger)
                    MessengerScreen(
                        navController = navController,
                        authViewModel = authViewModel,
                        uiStateDispatcher = uiStateDispatcher
                    )
                }
            }

            composable(
                route = Route.Messenger.Chat().route,
                arguments = listOf(navArgument(Constants.CHAT_NAV_ARG) {NavType.StringType})
            ) { entry ->
                ScreenContainer(
                    userCreds = userCreds,
                    navController = navController
                ) {
                    val chatId = entry.arguments?.getString(Constants.CHAT_NAV_ARG)
                    ChatScreen(
                        chatId = chatId,
                        chatViewModel = chatViewModel
                    )
                }
            }

            composable(
                route = Route.Profile().route,
                arguments = listOf(navArgument(Constants.PROFILE_NAV_ARG) { NavType.StringType })
            ) { entry ->
                ScreenContainer(
                    userCreds = userCreds,
                    navController = navController
                ) {
                    val context = LocalContext.current
                    runCatching {
                        val userId = entry.arguments?.getString(Constants.PROFILE_NAV_ARG) ?: ""
                        val user: MutableState<User?> = mutableStateOf(null)

                        if (userId == authorizedUser?.id?.toString())
                            user.value = authorizedUser
                        else userViewModel.getUserById(userId, user)


                        ProfileScreen(
                            navController = navController,
                            authViewModel = authViewModel,
                            user = user.value
                        )
                    }
                    .onFailure {
                        Toast.makeText(
                            context,
                            stringResource(id = R.string.toast_user_profile_error),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }

            composable(Route.FriendList.route) {
                ScreenContainer(
                    userCreds = userCreds,
                    navController = navController
                ) {
                    topBarTitle = stringResource(id = R.string.screen_title_friends)
                    FriendListScreen(
                        uiStateDispatcher = uiStateDispatcher,
                        authViewModel = authViewModel
                    )
                }
            }

            composable(Route.Notifications.route) {
                ScreenContainer(
                    userCreds = userCreds,
                    navController = navController
                ) {
                    topBarTitle = stringResource(id = R.string.screen_title_notifications)
                    NotificationScreen(navController)
                }
            }

            composable(Route.EditUserData.route) {
                ScreenContainer(
                    userCreds = userCreds,
                    navController = navController
                ) {
                    topBarTitle = stringResource(id = R.string.screen_title_edit_profile)
                    EditUserProfileScreen(
                        authViewModel = authViewModel,
                        authorizedUser = authorizedUser
                    )
                }
            }

            composable(Route.Settings.route) {
                ScreenContainer(
                    userCreds = userCreds,
                    navController = navController
                ) {
                    topBarTitle = stringResource(id = R.string.screen_title_settings)
                    SettingsScreen(authViewModel = authViewModel)
                }
            }

            composable(
                route = "${Route.Gallery.route}/{${Constants.GALLERY_INITIAL_PAGE_NAV_ARG}}",
                arguments = listOf(navArgument(Constants.GALLERY_INITIAL_PAGE_NAV_ARG) {NavType.StringType})
            ) { entry ->
                ScreenContainer(
                    userCreds = userCreds,
                    navController = navController
                ) {
                    topBarTitle = null
                    val images = uiStateDispatcher.uiState.collectAsState().value.galleryImageUrls
                    val initialPage = entry.arguments?.getString(Constants.GALLERY_INITIAL_PAGE_NAV_ARG)?.toInt() ?: 0
                    GalleryScreen(images = images, initialPage = initialPage)
                }
            }

            composable(Route.CreatePost.route) {
                ScreenContainer(
                    userCreds = userCreds,
                    navController = navController
                ) {
                    topBarTitle = stringResource(id = R.string.screen_title_create_post)
                    CreatePostScreen()
                }
            }
        }
    }
}


@Composable
private fun ScreenContainer(
    userCreds: UserPreferences?,
    navController: NavHostController,
    screen: @Composable () -> Unit = {}
) {
    if (userCreds?.accessToken != null) screen()
    else navController.navigate(Route.Authentication.route)
}