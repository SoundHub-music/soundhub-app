package com.soundhub.presentation.layout

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.soundhub.domain.model.MusicEntity
import com.soundhub.presentation.shared.buttons.FloatingNextButton
import com.soundhub.presentation.shared.containers.ContentContainer
import com.soundhub.presentation.shared.layouts.music_preferences.components.MusicItemPlate
import com.soundhub.presentation.shared.loaders.CircleLoader

@Composable
fun <T> PostRegisterGridLayout(
	items: List<MusicEntity<T>>,
	chosenItems: List<MusicEntity<T>>,
	isLoading: Boolean = true,
	title: String,
	onItemPlateClick: (isChosen: Boolean, item: MusicEntity<T>) -> Unit,
	onNextButtonClick: () -> Unit = {},
	lazyGridState: LazyGridState = rememberLazyGridState(),
	topContent: @Composable ColumnScope.() -> Unit = {},
) {
	ContentContainer(
		modifier = Modifier
			.background(MaterialTheme.colorScheme.background)
			.fillMaxSize(),
	) {
		Column(
			horizontalAlignment = Alignment.CenterHorizontally,
			modifier = Modifier.fillMaxSize()
		) {
			Text(
				modifier = Modifier
					.fillMaxWidth()
					.padding(start = 15.dp, top = 20.dp, end = 15.dp, bottom = 30.dp),
				text = title,
				fontSize = 32.sp,
				lineHeight = 42.sp,
				color = MaterialTheme.colorScheme.onSecondaryContainer,
				fontWeight = FontWeight.ExtraBold
			)

			topContent()

			if (isLoading) CircleLoader()
			else LazyVerticalGrid(
				state = lazyGridState,
				columns = GridCells.Adaptive(minSize = 100.dp),
				contentPadding = PaddingValues(all = 10.dp),
				content = {
					items(items = items, key = { it.id as Any }) { item ->
						MusicItemPlate(
							modifier = Modifier.padding(bottom = 20.dp),
							caption = item.name ?: "",
							thumbnailUrl = item.cover,
							onClick = { isChosen -> onItemPlateClick(isChosen, item) },
							isChosen = item.id in chosenItems.map { it.id },
							width = 90.dp,
							height = 90.dp
						)
					}
				}
			)
		}

		FloatingNextButton(
			modifier = Modifier
				.align(Alignment.BottomEnd)
				.padding(16.dp),
			onClick = onNextButtonClick,
			isLoading = isLoading
		)
	}
}