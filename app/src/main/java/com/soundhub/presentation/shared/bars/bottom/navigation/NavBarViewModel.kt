package com.soundhub.presentation.shared.bars.bottom.navigation

import android.util.Log
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AccountCircle
import androidx.compose.material.icons.rounded.Email
import androidx.compose.material.icons.rounded.Home
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import com.soundhub.R
import com.soundhub.Route
import com.soundhub.data.local_database.dao.UserDao
import com.soundhub.domain.model.User
import com.soundhub.presentation.shared.bars.bottom.navigation.model.NavBarIconType
import com.soundhub.presentation.shared.bars.bottom.navigation.model.NavBarMenuItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NavBarViewModel @Inject constructor(
	private val userDao: UserDao
) : ViewModel() {
	private val _selectedItem = MutableStateFlow<String?>(null)
	private val _menuItems = MutableStateFlow(emptyList<NavBarMenuItem>())

	val menuItems: StateFlow<List<NavBarMenuItem>> = _menuItems.asStateFlow()
	val selectedItem: StateFlow<String?> = _selectedItem.asStateFlow()

	init {
		viewModelScope.launch {
			loadNavBarItems()
		}
	}

	suspend fun loadNavBarItems() {
		val authorizedUser: User? = userDao.getCurrentUser()
		val navBarButtons = listOf(
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
				route = Route.Profile.getStringRouteWithNavArg(authorizedUser?.id.toString()),
				icon = NavBarIconType.VectorIcon(Icons.Rounded.AccountCircle)
			)
		)

		_menuItems.update { navBarButtons }
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