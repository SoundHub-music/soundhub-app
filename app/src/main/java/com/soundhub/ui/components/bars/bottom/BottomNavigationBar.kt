package com.soundhub.ui.components.bars.bottom

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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import com.soundhub.ui.components.icons.QueueMusic
import com.soundhub.Route
import com.soundhub.data.model.User
import com.soundhub.ui.messenger.MessengerViewModel
import com.soundhub.ui.viewmodels.UiStateDispatcher
import java.util.UUID

@Composable
fun BottomNavigationBar(
    navController: NavController,
    user: User,
    messengerViewModel: MessengerViewModel,
    uiStateDispatcher: UiStateDispatcher
) {
    val currentRoute by uiStateDispatcher.currentRoute.collectAsState()
    val unreadMessageCount = messengerViewModel
        .messengerUiState
        .collectAsState()
        .value
        .unreadMessagesTotal

    val selectedItemState: MutableState<String> = remember {
        mutableStateOf(currentRoute ?: Route.Postline.route)
    }

    LaunchedEffect(key1 = selectedItemState) {
        Log.d("BottomNavigationBar", selectedItemState.value)
    }

    NavigationBar(
        modifier = Modifier
            .padding(top = 10.dp, bottom = 10.dp, start = 16.dp, end = 16.dp)
            .clip(RoundedCornerShape(16.dp))
            .shadow(
                elevation = 4.dp,
                spotColor = Color(0x40000000),
                ambientColor = Color(0x40000000)
            ),
        containerColor = MaterialTheme.colorScheme.primaryContainer,
        contentColor = MaterialTheme.colorScheme.primary,
    ) {
        getNavBarItems(
            unreadMessageCount = unreadMessageCount,
            userId = user.id
        ).forEach { menuItem ->
            Log.d("BottomNavigationBar", "menuItem: $menuItem")
            NavigationBarItem(
                icon = menuItem.icon,
                selected = selectedItemState.value == menuItem.route,
                onClick = { onMenuItemClick(selectedItemState, menuItem, navController) }
            )
        }
    }
}

private fun onMenuItemClick(
    selectedItemState: MutableState<String>,
    menuItem: NavBarItem,
    navController: NavController
) {
    selectedItemState.value =  menuItem.route
    navController.navigate(menuItem.route) {
        popUpTo(navController.graph.findStartDestination().id) {
            saveState = true
            inclusive = true
        }

        launchSingleTop = true
        restoreState = true
    }
}

private fun getNavBarItems(
    unreadMessageCount: Int = 0,
    userId: UUID
): List<NavBarItem> {
    return listOf(
        NavBarItem(
            route = Route.Postline.route,
            icon = { Icon(Icons.Rounded.Home, contentDescription = "Home") },
        ),
        NavBarItem(
            route = Route.Music.route,
            icon = { Icon(Icons.Rounded.QueueMusic, contentDescription = "Music") },
        ),
        NavBarItem(
            route = Route.Messenger.route,
            icon = {
                if (unreadMessageCount != 0)
                    BadgedBox(badge = { Badge { Text(text = unreadMessageCount.toString()) } }) {
                        Icon(Icons.Rounded.Email, contentDescription = "Messenger")
                    }
                else Icon(Icons.Rounded.Email, contentDescription = "Messenger")
            },
        ),
        NavBarItem(
            route = Route.Profile(userId.toString()).route,
            icon = { Icon(Icons.Rounded.AccountCircle, contentDescription = "Profile") }
        )
    )
}