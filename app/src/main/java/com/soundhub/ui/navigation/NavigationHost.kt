package com.soundhub.ui.navigation

import android.content.Context
import android.util.Log
import android.widget.Toast
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
import androidx.compose.ui.platform.LocalContext
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
import com.soundhub.data.enums.ApiStatus
import com.soundhub.data.model.User
import com.soundhub.ui.authentication.AuthenticationScreen
import com.soundhub.ui.authentication.AuthenticationViewModel
import com.soundhub.ui.authentication.states.UserState
import com.soundhub.ui.authentication.postregistration.ChooseArtistsScreen
import com.soundhub.ui.authentication.postregistration.ChooseGenresScreen
import com.soundhub.ui.authentication.postregistration.FillUserDataScreen
import com.soundhub.ui.authentication.postregistration.RegistrationViewModel
import com.soundhub.ui.post_editor.PostEditorScreen
import com.soundhub.ui.edit_profile.EditUserProfileScreen
import com.soundhub.ui.edit_profile.EditUserProfileViewModel
import com.soundhub.ui.friends.FriendsScreen
import com.soundhub.ui.gallery.GalleryScreen
import com.soundhub.ui.messenger.MessengerScreen
import com.soundhub.ui.messenger.MessengerViewModel
import com.soundhub.ui.messenger.chat.ChatScreen
import com.soundhub.ui.music.MusicScreen
import com.soundhub.ui.music.MusicViewModel
import com.soundhub.ui.notifications.NotificationScreen
import com.soundhub.ui.postline.PostLineScreen
import com.soundhub.ui.profile.ProfileScreen
import com.soundhub.ui.settings.SettingsScreen
import com.soundhub.ui.viewmodels.UiStateDispatcher
import com.soundhub.ui.viewmodels.UserViewModel
import com.soundhub.utils.Constants
import kotlinx.coroutines.flow.firstOrNull
import java.util.UUID

@Composable
fun NavigationHost(
    navController: NavHostController,
    padding: PaddingValues,
    authViewModel: AuthenticationViewModel,
    registrationViewModel: RegistrationViewModel,
    uiStateDispatcher: UiStateDispatcher,
    messengerViewModel: MessengerViewModel,
    editUserProfileViewModel: EditUserProfileViewModel,
    topBarTitle: MutableState<String?>,
    authorizedUser: UserState
) {
    var startDestination: String by rememberSaveable { mutableStateOf(Route.Authentication.route) }

    val userCreds: UserPreferences? by authViewModel.userCreds.collectAsState(initial = null)
    val userViewModel: UserViewModel = hiltViewModel()

    val navBackStackEntry: NavBackStackEntry? by navController.currentBackStackEntryAsState()
    val currentRoute: String? = navBackStackEntry?.destination?.route

    val context: Context = LocalContext.current

    LaunchedEffect(key1 = currentRoute) {
        Log.d("HomeScreen", "current_route: $currentRoute")
        Log.d("HomeScreen", "authorized_user: $authorizedUser")
        Log.d("HomeScreen", "user_creds: ${userCreds.toString()}")
    }

    LaunchedEffect(key1 = userCreds?.accessToken) {
        startDestination = defineStartDestination(
            context = context,
            authorizedUser = authorizedUser,
            userCreds = userCreds
        )
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
                    uiStateDispatcher = uiStateDispatcher,
                    currentUser = authorizedUser.current
                )
            }
        }

        composable(Route.Music.route) {
            ScreenContainer(
                userCreds = userCreds,
                navController = navController
            ) {
                topBarTitle.value = stringResource(id = R.string.screen_title_music)
                MusicScreen(
                    navController = navController,
                    musicViewModel = MusicViewModel()
                )
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
                    messengerViewModel = messengerViewModel,
                )
            }
        }

        composable(
            route = Route.Messenger.Chat().route,
            arguments = listOf(navArgument(Constants.CHAT_NAV_ARG) { NavType.StringType})
        ) { entry ->
            runCatching {
                val argument: String? = entry.arguments?.getString(Constants.CHAT_NAV_ARG)
                val chatId: UUID = UUID.fromString(argument)
                
                ScreenContainer(
                    userCreds = userCreds,
                    navController = navController
                ) {
                    ChatScreen(
                        chatId = chatId,
                        authViewModel = authViewModel,
                        navController = navController,
                        uiStateDispatcher = uiStateDispatcher
                    )
                }
            }
        }

        composable(
            route = Route.Profile().route,
            arguments = listOf(navArgument(Constants.PROFILE_NAV_ARG) { NavType.StringType })
        ) { entry ->
            runCatching {
                authorizedUser.current?.let { currentUser ->
                    val argument = entry.arguments?.getString(Constants.PROFILE_NAV_ARG)
                    val userId: UUID? = argument?.let { UUID.fromString(argument) }
                    var user: User? by remember { mutableStateOf(null) }

                    Log.d("NavigationHost", "Profile[userId]: $userId")

                    LaunchedEffect(key1 = true) {
                        user = if (userId == authorizedUser.current.id)
                            currentUser
                        else userId?.let { userId ->
                            userViewModel
                                .getUserById(userId)
                                .firstOrNull()
                        }
                    }

                    ScreenContainer(
                        userCreds = userCreds,
                        navController = navController
                    ) {
                        ProfileScreen(
                            navController = navController,
                            authViewModel = authViewModel,
                            uiStateDispatcher = uiStateDispatcher,
                            user = user
                        )
                    }
                }
            }.onFailure {
                Log.d("NavigationHost", "Profile: ${it.stackTraceToString()}")
            }
        }

        composable(Route.FriendList.route) {
            ScreenContainer(
                userCreds = userCreds,
                navController = navController
            ) {
                topBarTitle.value = stringResource(id = R.string.screen_title_friends)
                FriendsScreen(
                    uiStateDispatcher = uiStateDispatcher,
                    navController = navController,
                    authorizedUser = authorizedUser.current
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
                    authorizedUser = authorizedUser.current,
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
                val images = uiStateDispatcher.uiState
                    .collectAsState()
                    .value
                    .galleryImageUrls

                val initialPage = entry.arguments
                    ?.getString(Constants.GALLERY_INITIAL_PAGE_NAV_ARG)
                    ?.toInt() ?: 0

                GalleryScreen(
                    images = images,
                    initialPage = initialPage,
                    uiStateDispatcher = uiStateDispatcher
                )
            }
        }

        // post creating route
        composable(route = Route.PostEditor.createPostRoute) {
            ScreenContainer(
                userCreds = userCreds,
                navController = navController
            ) {
                topBarTitle.value = stringResource(id = R.string.screen_title_create_post)
                PostEditorScreen(user = authorizedUser)
            }
        }

        // post editing route
        composable(
            route = Route.PostEditor().route,
            arguments = listOf(navArgument(Constants.POST_EDITOR_NAV_ARG) {
                NavType.StringType
            })
        ) { entry ->
            runCatching {
                val postArgument = entry.arguments?.getString(Constants.POST_EDITOR_NAV_ARG)
                val postId: UUID? = postArgument?.let { UUID.fromString(it) }

                ScreenContainer(
                    userCreds = userCreds,
                    navController = navController
                ) {
                    topBarTitle.value = stringResource(id = R.string.screen_title_create_post)
                    PostEditorScreen(
                        user = authorizedUser,
                        postId = postId
                    )
                }
            }
        }
    }
}

private fun defineStartDestination(
    context: Context,
    authorizedUser: UserState,
    userCreds: UserPreferences?
): String {
    val authErrorMessage: String = context.getString(R.string.toast_authorization_error)
    if (authorizedUser.current == null && authorizedUser.status == ApiStatus.ERROR)
        Toast.makeText(
            context,
            authErrorMessage,
            Toast.LENGTH_SHORT
        ).show()

    return if (
        userCreds?.accessToken.isNullOrEmpty()
    ) Route.Authentication.route
    else Route.Postline.route
}