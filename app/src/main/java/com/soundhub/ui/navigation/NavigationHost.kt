package com.soundhub.ui.navigation

import android.util.Log
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.navArgument
import com.soundhub.R
import com.soundhub.Route
import com.soundhub.data.datastore.UserPreferences
import com.soundhub.data.model.User
import com.soundhub.ui.authentication.AuthenticationScreen
import com.soundhub.ui.authentication.AuthenticationViewModel
import com.soundhub.ui.authentication.postregistration.ChooseArtistsScreen
import com.soundhub.ui.authentication.postregistration.ChooseGenresScreen
import com.soundhub.ui.authentication.postregistration.FillUserDataScreen
import com.soundhub.ui.authentication.postregistration.RegistrationViewModel
import com.soundhub.ui.create_post.CreatePostScreen
import com.soundhub.ui.edit_profile.EditUserProfileScreen
import com.soundhub.ui.edit_profile.EditUserProfileViewModel
import com.soundhub.ui.friends.FriendListScreen
import com.soundhub.ui.gallery.GalleryScreen
import com.soundhub.ui.messenger.MessengerScreen
import com.soundhub.ui.messenger.MessengerViewModel
import com.soundhub.ui.messenger.chat.ChatScreen
import com.soundhub.ui.messenger.chat.ChatViewModel
import com.soundhub.ui.music.MusicScreen
import com.soundhub.ui.notifications.NotificationScreen
import com.soundhub.ui.postline.PostLineScreen
import com.soundhub.ui.profile.ProfileScreen
import com.soundhub.ui.settings.SettingsScreen
import com.soundhub.ui.viewmodels.UiStateDispatcher
import com.soundhub.ui.viewmodels.UserViewModel
import com.soundhub.utils.Constants
import kotlinx.coroutines.flow.firstOrNull

@Composable
fun NavigationHost(
    navController: NavHostController,
    padding: PaddingValues,
    authViewModel: AuthenticationViewModel,
    registrationViewModel: RegistrationViewModel,
    uiStateDispatcher: UiStateDispatcher,
    chatViewModel: ChatViewModel,
    messengerViewModel: MessengerViewModel,
    editUserProfileViewModel: EditUserProfileViewModel,
    topBarTitle: MutableState<String?>
) {
    var startDestination: String by rememberSaveable { mutableStateOf(Route.Authentication.route) }

    val authorizedUser: User? by authViewModel.userInstance.collectAsState(initial = null)
    val userCreds: UserPreferences? by authViewModel.userCreds.collectAsState(initial = null)
    val userViewModel: UserViewModel = hiltViewModel()

    val navBackStackEntry: NavBackStackEntry? by navController.currentBackStackEntryAsState()
    val currentRoute: String? = navBackStackEntry?.destination?.route

    LaunchedEffect(key1 = currentRoute) {
        Log.d("HomeScreen", "current_route: $currentRoute")
        Log.d("HomeScreen", "authorized_user: ${authorizedUser.toString()}")
        Log.d("HomeScreen", "user_creds: ${userCreds.toString()}")
    }

    LaunchedEffect(key1 = userCreds?.accessToken) {
        startDestination = if (
            userCreds?.accessToken != null &&
            userCreds!!.accessToken?.isNotEmpty() == true
        ) Route.Postline.route
        else Route.Authentication.route
    }

    NavHost(
        modifier = Modifier.padding(padding),
        navController = navController,
        startDestination = startDestination
    ) {
        composable(Route.Authentication.route) {
            topBarTitle.value = null
            AuthenticationScreen(
                authViewModel = authViewModel,
                registrationViewModel = registrationViewModel
            )
        }

        composable(
            route = "${Route.Authentication.route}/{${Constants.POST_REGISTER_NAV_ARG}}",
            arguments = listOf(navArgument(Constants.POST_REGISTER_NAV_ARG) { NavType.StringType })
        ) { entry ->
            val nestedRoute =
                "${Route.Authentication.route}/${entry.arguments?.getString(Constants.POST_REGISTER_NAV_ARG)}"

            when (nestedRoute) {
                Route.Authentication.ChooseGenres.route -> ChooseGenresScreen(
                    registrationViewModel = registrationViewModel
                )

                Route.Authentication.ChooseArtists.route -> ChooseArtistsScreen(
                    registrationViewModel = registrationViewModel
                )

                Route.Authentication.FillUserData.route -> FillUserDataScreen(
                    registrationViewModel = registrationViewModel
                )

                else -> navController.navigate(Route.Authentication.route)
            }
        }

        composable(Route.Postline.route) {
            ScreenContainer(
                userCreds = userCreds,
                navController = navController
            ) {
                topBarTitle.value = stringResource(id = R.string.screen_title_postline)
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
                topBarTitle.value = stringResource(id = R.string.screen_title_music)
                MusicScreen(navController = navController)
            }
        }

        composable(Route.Messenger.route) {
            ScreenContainer(
                userCreds = userCreds,
                navController = navController
            ) {
                topBarTitle.value = stringResource(id = R.string.screen_title_messenger)
                MessengerScreen(
                    navController = navController,
                    authViewModel = authViewModel,
                    uiStateDispatcher = uiStateDispatcher,
                    messengerViewModel = messengerViewModel
                )
            }
        }

        composable(
            route = Route.Messenger.Chat().route,
            arguments = listOf(navArgument(Constants.CHAT_NAV_ARG) { NavType.StringType})
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
            val userId = entry.arguments?.getString(Constants.PROFILE_NAV_ARG) ?: ""
            val user: MutableState<User?> = remember { mutableStateOf(null) }
            Log.d("UserProfile", "userId: $userId")

            LaunchedEffect(key1 = true) {
                if (userId == authorizedUser?.id?.toString())
                    user.value = authorizedUser
                else user.value = userViewModel.getUserById(userId).firstOrNull()
            }

            ScreenContainer(
                userCreds = userCreds,
                navController = navController
            ) {
                ProfileScreen(
                    navController = navController,
                    authViewModel = authViewModel,
                    user = user.value
                )
            }
        }

        composable(Route.FriendList.route) {
            ScreenContainer(
                userCreds = userCreds,
                navController = navController
            ) {
                topBarTitle.value = stringResource(id = R.string.screen_title_friends)
                FriendListScreen(
                    uiStateDispatcher = uiStateDispatcher,
                    authViewModel = authViewModel,
                    navController = navController
                )
            }
        }

        composable(Route.Notifications.route) {
            ScreenContainer(
                userCreds = userCreds,
                navController = navController
            ) {
                topBarTitle.value = stringResource(id = R.string.screen_title_notifications)
                NotificationScreen(navController)
            }
        }

        composable(Route.EditUserData.route) {
            ScreenContainer(
                userCreds = userCreds,
                navController = navController
            ) {
                topBarTitle.value = stringResource(id = R.string.screen_title_edit_profile)
                EditUserProfileScreen(
                    authorizedUser = authorizedUser,
                    editUserProfileViewModel = editUserProfileViewModel,
                    authViewModel = authViewModel
                )
            }
        }

        composable(Route.Settings.route) {
            ScreenContainer(
                userCreds = userCreds,
                navController = navController
            ) {
                topBarTitle.value = stringResource(id = R.string.screen_title_settings)
                SettingsScreen(authViewModel = authViewModel)
            }
        }

        composable(
            route = "${Route.Gallery.route}/{${Constants.GALLERY_INITIAL_PAGE_NAV_ARG}}",
            arguments = listOf(navArgument(Constants.GALLERY_INITIAL_PAGE_NAV_ARG) { NavType.StringType})
        ) { entry ->
            ScreenContainer(
                userCreds = userCreds,
                navController = navController
            ) {
                topBarTitle.value = null
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
                topBarTitle.value = stringResource(id = R.string.screen_title_create_post)
                CreatePostScreen()
            }
        }
    }
}