package com.soundhub.ui.components

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.datastore.preferences.core.Preferences
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import com.soundhub.R
import com.soundhub.ui.authentication.AuthenticationViewModel
import com.soundhub.ui.authentication.LoginScreen
import com.soundhub.ui.components.icons.queueMusicIconRounded
import com.soundhub.ui.mainActivity.MainViewModel
import com.soundhub.ui.messenger.MessengerScreen
import com.soundhub.ui.music.MusicScreen
import com.soundhub.ui.postline.PostLineScreen
import com.soundhub.ui.profile.ProfileScreen
import com.soundhub.utils.Routes
import com.soundhub.utils.UiEvent
import androidx.compose.runtime.State
import androidx.datastore.preferences.core.stringPreferencesKey


@Preview
@Composable
fun AppContainer(
    modifier: Modifier = Modifier,
) {
    val authViewModel: AuthenticationViewModel = hiltViewModel()
    val mainViewModel: MainViewModel = hiltViewModel()

    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val isLoggedIn by authViewModel.isLoggedIn

    val context = LocalContext.current
    val currentRoute = navBackStackEntry?.destination?.route

    val userCreds: State<Preferences?> = authViewModel.userCreds.collectAsState(initial = null)
    val startDestination = getStartDestination(isLoggedIn, mainViewModel)

    LaunchedEffect(key1 = userCreds.value) {
        Log.d("creds", userCreds.value?.get(stringPreferencesKey("user_email")) ?: "null")
    }

    // it works every time when uiEvent comes or changes
    LaunchedEffect(mainViewModel.uiEvent) {
        mainViewModel.uiEvent.collect { event ->
            when(event) {
                is UiEvent.ShowToast -> Toast.makeText(
                    context, event.message, Toast.LENGTH_SHORT
                ).show()
                is UiEvent.Navigate -> navController.navigate(event.route.route)
                else -> Unit
            }
        }
    }

    Scaffold(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        bottomBar = {
            if (currentRoute != Routes.Authentication.route)
                BottomNavigationBar(items = getNavBarItems(), navController = navController)
        }
    ) {
        NavHost(
            modifier = Modifier.padding(it),
            navController = navController,
            startDestination = startDestination
        ) {
            navigation(
                route = Routes.AppStart.route,
                startDestination = Routes.Authentication.route
            ) {
                composable(Routes.Authentication.route) {
                    LoginScreen { route -> navController.navigate(route) }
                }
            }

            navigation(
                route = Routes.Authenticated.route,
                startDestination = Routes.Postline.route
            ) {
                composable(Routes.Postline.route) {
                    ScreenContainer(stringResource(id = R.string.screen_title_postline)) {
                        PostLineScreen { route -> navController.navigate(route) }
                    }
                }

                composable(Routes.Music.route) {
                    ScreenContainer(stringResource(id = R.string.screen_title_music)) {
                        MusicScreen { route -> navController.navigate(route) }
                    }
                }

                composable(Routes.Messenger.route) {
                    ScreenContainer(stringResource(id = R.string.screen_title_messenger)) {
                        MessengerScreen { route -> navController.navigate(route) }
                    }
                }

                composable(Routes.Profile.route) {
                    ProfileScreen { route -> navController.navigate(route) }
                }
            }
        }
    }
}

fun getStartDestination(isLoggedIn: Boolean, viewModel: MainViewModel): String {
    if (isLoggedIn) {
        viewModel.onEvent(UiEvent.Navigate(Routes.Authenticated))
        return Routes.Authenticated.route
    }

    viewModel.onEvent(UiEvent.Navigate(Routes.AppStart))
    return Routes.AppStart.route
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

@Composable
fun ScreenContainer(
    pageName: String?,
    content: @Composable () -> Unit) {

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        AppHeader(pageName, Modifier.padding(0.dp))
        content()
    }
}