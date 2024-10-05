package com.soundhub.ui.shared.bars.bottom

import android.util.Log
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AccountCircle
import androidx.compose.material.icons.rounded.Email
import androidx.compose.material.icons.rounded.Home
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import com.soundhub.R
import com.soundhub.Route
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class NavBarViewModel @Inject constructor() : ViewModel() {
	private val _selectedItem = MutableStateFlow<String?>(null)
	val selectedItem = _selectedItem.asStateFlow()

	fun getNavBarItems(userId: UUID?): List<NavBarMenuItem> {
		val navBarButtons = mutableListOf(
			NavBarMenuItem(
				route = Route.PostLine.route,
				icon = NavBarIconType.VectorIcon(Icons.Rounded.Home),
			),
			NavBarMenuItem(
				route = Route.Music.route,
				icon = NavBarIconType.PainterIcon(R.drawable.round_queue_music_24)
			),
			NavBarMenuItem(
				route = Route.Messenger.route,
				icon = NavBarIconType.VectorIcon(Icons.Rounded.Email)
			),
			NavBarMenuItem(
				route = Route.Profile.getStringRouteWithNavArg(userId.toString()),
				icon = NavBarIconType.VectorIcon(Icons.Rounded.AccountCircle)
			)
		)


		return navBarButtons
	}

	fun onMenuItemClick(
		menuItem: NavBarMenuItem,
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