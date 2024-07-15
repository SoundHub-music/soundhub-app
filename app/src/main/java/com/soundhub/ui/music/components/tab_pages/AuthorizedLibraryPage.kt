package com.soundhub.ui.music.components.tab_pages

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import com.soundhub.data.model.LibraryItemData
import com.soundhub.ui.music.components.LibraryItem

@Composable
fun AuthorizedLibraryPage(items: List<LibraryItemData>) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        items(items = items , key = { it.title }) { item ->
            LibraryItem(
                title = item.title,
                icon = item.icon,
                contentDescription = item.contentDescription,
            )
        }
    }
}