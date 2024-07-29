package com.soundhub.ui.shared.bars.bottom

import android.util.Log
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AccountCircle
import androidx.compose.material.icons.rounded.Email
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBarItem
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import com.soundhub.ui.shared.icons.QueueMusic
import com.soundhub.Route
import com.soundhub.data.model.User
import com.soundhub.data.states.MessengerUiState
import com.soundhub.ui.pages.messenger.MessengerViewModel
import com.soundhub.data.states.UiState
import com.soundhub.ui.viewmodels.UiStateDispatcher
import java.util.UUID

@Composable
fun BottomNavigationBar(
    navController: NavController,
    messengerViewModel: MessengerViewModel,
    uiStateDispatcher: UiStateDispatcher,
    navBarViewModel: NavBarViewModel = hiltViewModel()
) {
    val uiState by uiStateDispatcher.uiState.collectAsState(initial = UiState())
    val authorizedUser: User? = uiState.authorizedUser
    val navRoute: NavBackStackEntry? by navController.currentBackStackEntryAsState()
    val defaultSelectedItem: String? = navRoute?.destination?.route

//    val selectedItemState: MutableState<String?> = remember { mutableStateOf(defaultSelectedItem) }
    val selectedItemState by navBarViewModel.selectedItem.collectAsState()
    val receivedMessageChannel = uiStateDispatcher.receivedMessages
    val navBarItems: List<NavBarItem> = remember(authorizedUser?.id) {
        navBarViewModel.getNavBarItems(authorizedUser?.id)
    }
    val navBarRoutes: List<String> = remember(navBarItems) { navBarItems.map { it.route } }

    LaunchedEffect(key1 = true) {
        receivedMessageChannel.collect { _ ->
            messengerViewModel.updateUnreadMessageCount()
        }
    }

    LaunchedEffect(key1 = uiState.currentRoute, key2 = navRoute) {
        val checkRoute: (String) -> Boolean = {
            route -> uiState.currentRoute?.contains(route) == true
        }
        val isInRoutesOrContains: Boolean = selectedItemState in navBarRoutes ||
                navBarRoutes.any { checkRoute(it) }

        val selectedItem = if (isInRoutesOrContains) {
            navBarRoutes.first { checkRoute(it) }
        } else null

        navBarViewModel.setSelectedItem(selectedItem)
        Log.d("BottomNavigationBar", "selected page: $selectedItemState")
    }

    NavigationBar(
        modifier = Modifier
            .padding(vertical = 10.dp, horizontal = 16.dp)
            .clip(RoundedCornerShape(16.dp))
            .shadow(
                elevation = 4.dp,
                spotColor = Color(0x40000000),
                ambientColor = Color(0x40000000)
            ),
        containerColor = MaterialTheme.colorScheme.primaryContainer,
        contentColor = MaterialTheme.colorScheme.primary,
    ) {
        navBarItems.forEach { menuItem ->
            Log.d("BottomNavigationBar", "menuItem: $menuItem")
            NavigationBarItem(
                icon = {
                    NavBarItemBadgeBox(
                        messengerViewModel = messengerViewModel,
                        menuItem = menuItem
                    )
                },
                selected = selectedItemState == menuItem.route,
                onClick = { navBarViewModel.onMenuItemClick(menuItem, navController) }
            )
        }
    }
}

@Composable
private fun NavBarItemBadgeBox(
    messengerViewModel: MessengerViewModel,
    menuItem: NavBarItem
) {
    val messengerUiState: MessengerUiState by messengerViewModel
        .messengerUiState
        .collectAsState()

    BadgedBox(
        badge = {
            if (messengerUiState.unreadMessagesCount > 0 && menuItem.route == Route.Messenger.route)
                Badge { Text(text = messengerUiState.unreadMessagesCount.toString()) }
        }
    ) { Icon(menuItem.icon, contentDescription = menuItem.route) }
}

private fun onMenuItemClick(
    selectedItemState: MutableState<String?>,
    menuItem: NavBarItem,
    navController: NavController
) {
    selectedItemState.value = menuItem.route
    Log.d("BottomNavigationBar", "onMenuItemClick: ${menuItem.route}")

    if (!menuItem.route.contains("null"))
        navController.navigate(menuItem.route) {
            popUpTo(navController.graph.findStartDestination().id) {
                saveState = true
                inclusive = true
            }

            launchSingleTop = true
            restoreState = true
        }
}


private fun getNavBarItems(userId: UUID?): List<NavBarItem> {
    val navBarButtons = mutableListOf(
        NavBarItem(
            route = Route.PostLine.route,
            icon = Icons.Rounded.Home,
        ),
        NavBarItem(
            route = Route.Music.route,
            icon = Icons.Rounded.QueueMusic,
        ),
        NavBarItem(
            route = Route.Messenger.route,
            icon = Icons.Rounded.Email
        ),
        NavBarItem(
            route = Route.Profile.getStringRouteWithNavArg(userId.toString()),
            icon = Icons.Rounded.AccountCircle
        )
    )


    return navBarButtons
}