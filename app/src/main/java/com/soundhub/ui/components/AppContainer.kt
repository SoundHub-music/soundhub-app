package com.soundhub.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AccountCircle
import androidx.compose.material.icons.rounded.Email
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.soundhub.ui.authentication.AuthenticationViewModel
import com.soundhub.ui.authentication.LoginScreen
import com.soundhub.ui.components.icons.queueMusicIconRounded
import com.soundhub.ui.messenger.MessengerScreen
import com.soundhub.ui.music.MusicScreen
import com.soundhub.ui.navigation.NavigationViewModel
import com.soundhub.ui.postline.PostLineScreen
import com.soundhub.ui.profile.ProfileScreen
import com.soundhub.utils.NamedScreens
import com.soundhub.utils.Routes
import com.soundhub.utils.UiEvent
import kotlinx.coroutines.flow.collect


@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun AppContainer(
    modifier: Modifier = Modifier,
) {
    val authViewModel: AuthenticationViewModel = hiltViewModel()
    val navController = rememberNavController()
    val isLoggedIn = authViewModel.isLoggedIn

    val navBarItems: List<NavigationItemApp> = mutableListOf(
        NavigationItemApp(
            route = NamedScreens.POSTLINE,
            icon = { Icon(Icons.Rounded.Home, contentDescription = null) },
        ),
        NavigationItemApp(
            route = NamedScreens.MUSIC,
            icon = { Icon(queueMusicIconRounded(), contentDescription = null) },
        ),
        NavigationItemApp(
            route = NamedScreens.MESSENGER,
            icon = {
                BadgedBox(badge = { Badge { Text(text = "5")} }) {
                    Icon(Icons.Rounded.Email, contentDescription = null)
                }
            },
        ),
        NavigationItemApp(
            route = NamedScreens.PROFILE,
            icon = { Icon(Icons.Rounded.AccountCircle, contentDescription = null) }
        )
    )

//    val navModel: NavigationViewModel = hiltViewModel()
//    LaunchedEffect(key1 = "key") {
//        navModel.uiEvent.collect {event ->
//            when (event) {
//                is UiEvent.Navigate -> navController.navigate(event.route.name)
//                else -> Unit
//            }
//        }
//    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
    ) {
        if (isLoggedIn.value)
            AppHeader(pageName = "Главная")

        NavHost(
            navController = navController,
            modifier = Modifier.weight(1f),
            startDestination = if (isLoggedIn.value) Routes.POSTLINE.name else Routes.AUTHENTICATION.name
        ) {
            composable(Routes.AUTHENTICATION.name) {
                LoginScreen(
                    onNavigate = { navController.navigate(it) }
                )
            }
            composable(Routes.POSTLINE.name) {
                PostLineScreen(
                    onNavigate = { navController.navigate(it) },
                )
            }

            composable(Routes.MUSIC.name) {
                MusicScreen(
                    onNavigate = { navController.navigate(it) }
                )
            }

            composable(Routes.MESSENGER.name) {
                MessengerScreen(
                    onNavigate = { navController.navigate(it) }
                )
            }

            composable(Routes.PROFILE.name) {
                ProfileScreen(
                    onNavigate = { navController.navigate(it) }
                )
            }
        }
        if (isLoggedIn.value)
            NavigationBarContainer(items = navBarItems, navController)
    }
}