package com.soundhub.ui.music.components.layout

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import com.soundhub.data.model.MusicEntity
import com.soundhub.ui.components.containers.ContentContainer
import com.soundhub.ui.components.layouts.music_preferences.components.MusicItemPlate

@Composable
internal fun <T, U: MusicEntity<T>> MusicGridLayout(items: List<U>) {
    ContentContainer {
        LazyVerticalGrid(
            columns = GridCells.Adaptive(minSize = 100.dp),
            contentPadding = PaddingValues(20.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(items = items, key = { it.id.toString() }) { genre ->
                MusicItemPlate(
                    caption = genre.name ?: "",
                    clickable = false,
                    thumbnailUrl = genre.cover,
                    width = 92.dp,
                    height = 92.dp
                )
            }
        }
    }
}