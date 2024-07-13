package com.soundhub.ui.music.components.tab_pages

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.soundhub.ui.music.components.LibraryItem

@Composable
fun AuthorizedLibraryPage(
    modifier: Modifier = Modifier,
    items: List<LibraryItemData>,
    navController: NavHostController
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(top = 20.dp)
    ) {
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(items = items , key = { it.title }) { item ->
                LibraryItem(
                    title = item.title,
                    icon = item.icon,
                    contentDescription = item.contentDescription,
                    route = item.route,
                    navController = navController
                )
            }
        }
    }
}