package com.soundhub.ui.components

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AccountCircle
import androidx.compose.material.icons.rounded.Email
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import com.soundhub.ui.components.icons.queueMusicIconRounded
import com.soundhub.utils.Route


@Composable
fun BottomNavigationBar(navController: NavController) {
    val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route
    var selectedItem: Route by remember {
        mutableStateOf(Route.valueOf(currentRoute) ?: Route.Postline)
    }

    LaunchedEffect(currentRoute) {
        selectedItem = Route.valueOf(currentRoute) ?: Route.Postline
    }


    NavigationBar(
        modifier = Modifier
            .padding(top = 0.dp, bottom = 10.dp, start = 16.dp, end = 16.dp)
            .clip(RoundedCornerShape(16.dp))
            .shadow(
                elevation = 4.dp,
                spotColor = Color(0x40000000),
                ambientColor = Color(0x40000000)
            ),
        containerColor = MaterialTheme.colorScheme.primaryContainer,
        contentColor = Color.Cyan
    ) {
        getNavBarItems().forEach{ item ->
            NavigationBarItem(
                icon = item.icon,
                selected = selectedItem == item.route,
                onClick = {
                    selectedItem = item.route
                    navController.navigate(item.route.route) {
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

@OptIn(ExperimentalMaterial3Api::class)
private fun getNavBarItems(): List<NavigationItemApp> {
    return listOf(
        NavigationItemApp(
            route = Route.Postline,
            icon = { Icon(Icons.Rounded.Home, contentDescription = "Home") },
        ),
        NavigationItemApp(
            route = Route.Music,
            icon = { Icon(queueMusicIconRounded(), contentDescription = "Music") },
        ),
        NavigationItemApp(
            route = Route.Messenger,
            icon = {
                BadgedBox(badge = { Badge { Text(text = "5") } }) {
                    Icon(Icons.Rounded.Email, contentDescription = "Messenger")
                }
            },
        ),
        NavigationItemApp(
            route = Route.Profile,
            icon = { Icon(Icons.Rounded.AccountCircle, contentDescription = "Profile") }
        )
    )
}

data class NavigationItemApp(
    val route: Route = Route.Postline,
    val icon: @Composable () -> Unit,
    val label: String? = "",
)