package com.soundhub.ui.navigation

import android.util.Log
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import com.soundhub.data.datastore.model.UserPreferences
import com.soundhub.data.model.User
import com.soundhub.data.states.UiState
import com.soundhub.ui.pages.authentication.AuthenticationScreen
import com.soundhub.ui.pages.authentication.AuthenticationViewModel
import com.soundhub.ui.pages.authentication.registration.FillUserDataScreen
import com.soundhub.ui.pages.authentication.registration.PostRegisterChooseArtistsScreen
import com.soundhub.ui.pages.authentication.registration.PostRegisterChooseGenresScreen
import com.soundhub.ui.pages.authentication.registration.RegistrationViewModel
import com.soundhub.ui.pages.chat.ChatScreen
import com.soundhub.ui.pages.edit_profile.music.EditFavoriteArtistsScreen
import com.soundhub.ui.pages.edit_profile.music.EditFavoriteGenresScreen
import com.soundhub.ui.pages.edit_profile.music.EditMusicPreferencesViewModel
import com.soundhub.ui.pages.edit_profile.profile.EditUserProfileScreen
import com.soundhub.ui.pages.friends.FriendsScreen
import com.soundhub.ui.pages.friends.FriendsViewModel
import com.soundhub.ui.pages.messenger.MessengerScreen
import com.soundhub.ui.pages.messenger.MessengerViewModel
import com.soundhub.ui.pages.music.MusicScreen
import com.soundhub.ui.pages.music.MusicViewModel
import com.soundhub.ui.pages.music.components.tab_pages.library.FavoriteArtistsScreen
import com.soundhub.ui.pages.music.components.tab_pages.library.FavoriteGenresScreen
import com.soundhub.ui.pages.notifications.NotificationScreen
import com.soundhub.ui.pages.notifications.NotificationViewModel
import com.soundhub.ui.pages.post_editor.PostEditorScreen
import com.soundhub.ui.pages.postline.PostLineScreen
import com.soundhub.ui.pages.profile.ProfileScreen
import com.soundhub.ui.pages.profile.ProfileViewModel
import com.soundhub.ui.pages.settings.SettingsScreen
import com.soundhub.ui.shared.gallery.GalleryScreen
import com.soundhub.ui.viewmodels.MainViewModel
import com.soundhub.ui.viewmodels.UiStateDispatcher
import com.soundhub.utils.constants.Constants
import java.util.UUID

@Composable
fun NavigationHost(
	navController: NavHostController,
	padding: PaddingValues,
	authViewModel: AuthenticationViewModel,
	registrationViewModel: RegistrationViewModel,
	uiStateDispatcher: UiStateDispatcher,
	messengerViewModel: MessengerViewModel,
	notificationViewModel: NotificationViewModel,
	mainViewModel: MainViewModel,
	musicViewModel: MusicViewModel
) {
	val uiState: UiState by uiStateDispatcher.uiState.collectAsState(UiState())
	val authorizedUser: User? by remember(uiState.authorizedUser) {
		mutableStateOf(uiState.authorizedUser)
	}

	val userCreds: UserPreferences? by mainViewModel.userCreds.collectAsState(initial = null)
	val startDestination: String by mainViewModel.startDestination.collectAsState()

	val navBackStackEntry: NavBackStackEntry? by navController.currentBackStackEntryAsState()
	val currentRoute: String? = navBackStackEntry?.destination?.route

	val editMusicPrefViewModel: EditMusicPreferencesViewModel = hiltViewModel()

	LaunchedEffect(currentRoute) {
		Log.d("NavigationHost", "current_route: $currentRoute")
	}

	LaunchedEffect(authorizedUser) {
		val gson = mainViewModel.getGson()
		Log.d("NavigationHost", "authorized_user: ${gson.toJson(authorizedUser)}")
	}

	LaunchedEffect(userCreds) {
		Log.d("NavigationHost", "user_creds: $userCreds")
	}

	NavHost(
		modifier = Modifier.padding(padding),
		navController = navController,
		startDestination = startDestination
	) {
		composable(Route.Authentication.route) {
			mainViewModel.setTopBarTitle(null)
			AuthenticationScreen(
				authViewModel = authViewModel,
				registrationViewModel = registrationViewModel
			)
		}

		composable(
			route = Route.Authentication.withNavArg,
			arguments = listOf(navArgument(Constants.POST_REGISTER_NAV_ARG) { NavType.StringType })
		) { entry ->
			val nestedRoute =
				"${Route.Authentication.route}/${entry.arguments?.getString(Constants.POST_REGISTER_NAV_ARG)}"

			when (nestedRoute) {
				Route.Authentication.ChooseGenres.route -> PostRegisterChooseGenresScreen(
					registrationViewModel = registrationViewModel
				)

				Route.Authentication.ChooseArtists.route -> PostRegisterChooseArtistsScreen(
					registrationViewModel = registrationViewModel,
					uiStateDispatcher = uiStateDispatcher
				)

				Route.Authentication.FillUserData.route -> FillUserDataScreen(
					registrationViewModel = registrationViewModel
				)

				else -> navController.navigate(Route.Authentication.route)
			}
		}

		composable(Route.PostLine.route) {
			mainViewModel.setTopBarTitle(stringResource(id = R.string.screen_title_postline))
			PostLineScreen(
				navController = navController,
				uiStateDispatcher = uiStateDispatcher,
			)
		}

		composable(Route.Music.route) {
			mainViewModel.setTopBarTitle(stringResource(id = R.string.screen_title_music))
			MusicScreen(
				navController = navController,
				musicViewModel = musicViewModel,
				uiStateDispatcher = uiStateDispatcher
			)
		}

		composable(Route.Music.FavoriteGenres.route) {
			mainViewModel.setTopBarTitle(stringResource(id = R.string.screen_title_favorite_genres))
			FavoriteGenresScreen(musicViewModel)
		}

		composable(Route.Music.FavoriteArtists.route) {
			mainViewModel.setTopBarTitle(stringResource(id = R.string.screen_title_favorite_artists))
			FavoriteArtistsScreen(musicViewModel)
		}

		composable(Route.Messenger.route) {
			mainViewModel.setTopBarTitle(stringResource(id = R.string.screen_title_messenger))
			MessengerScreen(
				navController = navController,
				uiStateDispatcher = uiStateDispatcher,
				messengerViewModel = messengerViewModel,
			)
		}

		composable(
			route = Route.Messenger.Chat.route,
			arguments = listOf(navArgument(Constants.CHAT_NAV_ARG) { NavType.StringType })
		) { entry ->
			runCatching {
				val argument: String? = entry.arguments?.getString(Constants.CHAT_NAV_ARG)
				val chatId: UUID = UUID.fromString(argument)

				ChatScreen(
					chatId = chatId,
					navController = navController,
					uiStateDispatcher = uiStateDispatcher
				)
			}
				.onFailure { error ->
					Log.d("NavigationHost", "ChatScreen[error]: ${error.stackTraceToString()}")
				}
		}

		composable(
			route = Route.Profile.route,
			arguments = listOf(navArgument(Constants.PROFILE_NAV_ARG) { NavType.StringType })
		) { entry ->
			runCatching {
				val argument = entry.arguments?.getString(Constants.PROFILE_NAV_ARG)
				val userId: UUID? = argument?.let { UUID.fromString(argument) }
				val profileViewModel: ProfileViewModel = hiltViewModel()

				ProfileScreen(
					navController = navController,
					uiStateDispatcher = uiStateDispatcher,
					profileViewModel = profileViewModel,
					userId = userId
				)
			}
		}

		composable(
			route = Route.Profile.Friends.route,
			arguments = listOf(navArgument(Constants.PROFILE_NAV_ARG) { NavType.StringType })
		) { entry ->
			runCatching {
				val argument: String? = entry.arguments?.getString(Constants.PROFILE_NAV_ARG)
				val userId: UUID? = argument?.let { UUID.fromString(it) }
				val friendsViewModel: FriendsViewModel = hiltViewModel()

				mainViewModel.setTopBarTitle(stringResource(id = R.string.screen_title_friends))
				FriendsScreen(
					uiStateDispatcher = uiStateDispatcher,
					navController = navController,
					friendsViewModel = friendsViewModel,
					userId = userId
				)
			}
		}

		composable(Route.Notifications.route) {
			mainViewModel.setTopBarTitle(stringResource(id = R.string.screen_title_notifications))
			NotificationScreen(
				navController = navController,
				notificationViewModel = notificationViewModel
			)
		}

		composable(Route.EditUserData.route) {
			EditUserProfileScreen(
				authorizedUser = authorizedUser,
				navController = navController
			)
		}

		composable(Route.Settings.route) {
			mainViewModel.setTopBarTitle(stringResource(id = R.string.screen_title_settings))
			SettingsScreen(
				authViewModel = authViewModel,
				uiStateDispatcher = uiStateDispatcher
			)
		}

		composable(
			route = "${Route.Gallery.route}/{${Constants.GALLERY_INITIAL_PAGE_NAV_ARG}}",
			arguments = listOf(navArgument(Constants.GALLERY_INITIAL_PAGE_NAV_ARG) { NavType.StringType })
		) { entry ->
			mainViewModel.setTopBarTitle(null)
			val images = uiStateDispatcher.uiState
				.collectAsState(initial = UiState())
				.value
				.galleryImageUrls

			val initialPage: Int = entry.arguments
				?.getString(Constants.GALLERY_INITIAL_PAGE_NAV_ARG)
				?.toInt() ?: 0

			GalleryScreen(
				images = images,
				initialPage = initialPage,
				uiStateDispatcher = uiStateDispatcher
			)
		}

		// post creating route
		composable(route = Route.PostEditor.createPostRoute) {
			mainViewModel.setTopBarTitle(stringResource(id = R.string.screen_title_create_post))
			PostEditorScreen(profileOwner = authorizedUser)
		}

		// post editing route
		composable(
			route = Route.PostEditor.route,
			arguments = listOf(navArgument(Constants.POST_EDITOR_NAV_ARG) { NavType.StringType })
		) { entry ->
			runCatching {
				val postArgument = entry.arguments?.getString(Constants.POST_EDITOR_NAV_ARG)
				val postId: UUID? = postArgument?.let { UUID.fromString(it) }

				mainViewModel.setTopBarTitle(stringResource(id = R.string.screen_title_update_post))
				PostEditorScreen(
					profileOwner = authorizedUser,
					postId = postId
				)
			}
		}

		composable(Route.EditFavoriteGenres.route) {
			EditFavoriteGenresScreen(editMusicPrefViewModel)
		}

		composable(Route.EditFavoriteArtists.route) {
			EditFavoriteArtistsScreen(
				editMusicPrefViewModel = editMusicPrefViewModel,
				uiStateDispatcher = uiStateDispatcher
			)
		}
	}
}
