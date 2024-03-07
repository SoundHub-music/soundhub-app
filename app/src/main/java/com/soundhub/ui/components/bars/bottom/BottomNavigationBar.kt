package com.soundhub.ui.components.bars.bottom

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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import com.soundhub.ui.components.icons.QueueMusic
import com.soundhub.Route
import com.soundhub.data.model.User
import com.soundhub.ui.messenger.MessengerViewModel
import java.util.UUID

@Composable
fun BottomNavigationBar(
    navController: NavController,
    user: User? = null,
    messengerViewModel: MessengerViewModel = hiltViewModel()
) {
    val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route
    // plug variable
    val unreadMessageCount = 1
    var selectedItem: String by remember { mutableStateOf(currentRoute ?: Route.Postline.route) }

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
            userId = user?.id
        ).forEach{ item ->
            NavigationBarItem(
                icon = item.icon,
                selected = selectedItem == item.route,
                onClick = {
                    selectedItem = item.route
                    navController.navigate(item.route) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                            inclusive = true
                        }

                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
    }
}

private fun getNavBarItems(
    unreadMessageCount: Int = 0,
    userId: UUID?
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
            },
        ),
        NavBarItem(
            route = Route.Profile(userId?.toString()).route,
            icon = { Icon(Icons.Rounded.AccountCircle, contentDescription = "Profile") }
        )
    )
}

data class NavBarItem(
    val route: String = Route.Postline.route,
    val icon: @Composable () -> Unit,
)