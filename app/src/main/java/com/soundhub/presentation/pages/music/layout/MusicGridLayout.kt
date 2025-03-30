package com.soundhub.presentation.pages.music.layout

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import com.soundhub.domain.model.MusicEntity
import com.soundhub.presentation.shared.containers.ContentContainer
import com.soundhub.presentation.shared.layouts.music_preferences.components.MusicItemPlate

@Composable
internal fun <T, U : MusicEntity<T>> MusicGridLayout(items: List<U>) {
	ContentContainer {
		LazyVerticalGrid(
			columns = GridCells.Adaptive(minSize = 100.dp),
			contentPadding = PaddingValues(20.dp),
			verticalArrangement = Arrangement.spacedBy(20.dp),
			horizontalArrangement = Arrangement.spacedBy(10.dp)
		) {
			itemsIndexed(
				items = items,
				key = { index, item -> item.id.toString() }) { index, genre ->
				MusicItemPlate(
					index = index,
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