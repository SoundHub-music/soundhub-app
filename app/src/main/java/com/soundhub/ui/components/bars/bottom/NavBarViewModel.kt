package com.soundhub.ui.components.bars.bottom

import android.util.Log
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AccountCircle
import androidx.compose.material.icons.rounded.Email
import androidx.compose.material.icons.rounded.Home
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import com.soundhub.Route
import com.soundhub.ui.components.icons.QueueMusic
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class NavBarViewModel @Inject constructor(): ViewModel() {
    private val _selectedItem = MutableStateFlow<String?>(null)
    val selectedItem = _selectedItem.asStateFlow()

    fun getNavBarItems(userId: UUID?): List<NavBarItem> {
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

    fun onMenuItemClick(
        menuItem: NavBarItem,
        navController: NavController
    ) {
        _selectedItem.update { menuItem.route }
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

    fun setSelectedItem(value: String?) = _selectedItem.update { value }
}