package com.soundhub.ui.music.components.tab_pages

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.soundhub.data.model.LibraryItemData
import com.soundhub.ui.music.data.getMenuItems

@Composable
internal fun MusicLibraryPage(modifier: Modifier = Modifier) {
    val authorized = false
    val menuItems: List<LibraryItemData> = getMenuItems()

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(top = 20.dp),
        verticalArrangement = Arrangement.spacedBy(30.dp)
    ) {
        AuthorizedLibraryPage(menuItems)
        if (!authorized)
            UnauthorizedLibraryPage()
    }
}