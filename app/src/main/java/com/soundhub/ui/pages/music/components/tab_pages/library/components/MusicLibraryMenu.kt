package com.soundhub.ui.pages.music.components.tab_pages.library.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.soundhub.Route
import com.soundhub.data.model.LibraryItemData
import com.soundhub.ui.pages.music.MusicViewModel

@Composable
fun MusicLibraryMenu(
	musicViewModel: MusicViewModel,
	navController: NavHostController,
	isAuthorized: Boolean = false
) {
	val menuItems: List<LibraryItemData> = musicViewModel.getMenuItems()

	LazyColumn(
		verticalArrangement = Arrangement.spacedBy(10.dp)
	) {
		items(items = menuItems, key = { it.title }) { item ->
			var enabled = true

			if (Route.Music.Playlists.route == item.route)
				enabled = isAuthorized

			LibraryItem(
				title = item.title,
				icon = item.icon,
				enabled = enabled,
				contentDescription = item.contentDescription,
				onClick = { navController.navigate(item.route) }
			)
		}
	}
}