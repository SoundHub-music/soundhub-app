package com.soundhub.presentation.layout

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyGridScope
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import com.soundhub.domain.model.MusicEntity
import com.soundhub.presentation.shared.buttons.FloatingNextButton
import com.soundhub.presentation.shared.containers.ContentContainer
import com.soundhub.presentation.shared.layouts.music_preferences.components.MusicItemPlate
import com.soundhub.presentation.shared.loaders.CircleLoader

@Composable
fun <T : Any> PostRegisterGridLayout(
	items: List<MusicEntity<T>>? = null,
	pagedItems: LazyPagingItems<out MusicEntity<T>>? = null,
	chosenItems: List<MusicEntity<T>>,
	isLoading: Boolean = true,
	title: String,
	onItemPlateClick: (isChosen: Boolean, item: MusicEntity<T>) -> Unit,
	onNextButtonClick: () -> Unit = {},
	lazyGridState: LazyGridState = rememberLazyGridState(),
	topContent: @Composable ColumnScope.() -> Unit = {},
) {
	val loadState: CombinedLoadStates? = pagedItems?.loadState

	val isRefreshLoading: Boolean = loadState?.refresh is LoadState.Loading
	val isAppendLoading: Boolean = loadState?.append is LoadState.Loading
	val isLoading: Boolean = isRefreshLoading || isAppendLoading || isLoading

	ContentContainer(
		modifier = Modifier
			.background(MaterialTheme.colorScheme.background)
			.fillMaxSize()
	) {
		Column(
			horizontalAlignment = Alignment.CenterHorizontally,
			modifier = Modifier.fillMaxSize()
		) {
			Text(
				modifier = Modifier
					.fillMaxWidth()
					.padding(
						start = 15.dp,
						top = 20.dp,
						end = 15.dp,
						bottom = 30.dp
					),
				text = title,
				fontSize = 32.sp,
				lineHeight = 42.sp,
				color = MaterialTheme.colorScheme.onSecondaryContainer,
				fontWeight = FontWeight.ExtraBold
			)

			topContent()

			if (isRefreshLoading)
				CircleLoader(modifier = Modifier.padding(top = 20.dp))
			else LazyVerticalGrid(
				state = lazyGridState,
				columns = GridCells.Adaptive(minSize = 100.dp),
				contentPadding = PaddingValues(all = 10.dp),
				modifier = Modifier
					.fillMaxHeight()
					.padding(bottom = 50.dp),
				content = {
					gridContent(
						onItemPlateClick = onItemPlateClick,
						chosenItems = chosenItems,
						pagedItems = pagedItems,
						items = items,
					)
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

private fun <T : Any> LazyGridScope.gridContent(
	pagedItems: LazyPagingItems<out MusicEntity<T>>?,
	items: List<MusicEntity<T>>?,
	chosenItems: List<MusicEntity<T>>,
	onItemPlateClick: (isChosen: Boolean, item: MusicEntity<T>) -> Unit
) {
	val renderItem: @Composable (Int, MusicEntity<T>) -> Unit = { index, item ->
		GridItem(
			item = item,
			index = index,
			onItemPlateClick = onItemPlateClick,
			chosenItems = chosenItems
		)
	}

	if (pagedItems != null) {
		items(count = pagedItems.itemCount, key = { it }) { index ->
			val item = pagedItems[index] ?: return@items
			renderItem(index, item)
		}
	} else {
		itemsIndexed(items = items.orEmpty(), key = { _, item -> item.id }) { index, item ->
			renderItem(index, item)
		}
	}
}

@Composable
private fun <T : Any> GridItem(
	modifier: Modifier = Modifier,
	item: MusicEntity<T>,
	index: Int,
	onItemPlateClick: (isChosen: Boolean, item: MusicEntity<T>) -> Unit,
	chosenItems: List<MusicEntity<T>>
) {
	val isChosen by remember(chosenItems, item.id) {
		derivedStateOf { chosenItems.any { it.id == item.id } }
	}

	MusicItemPlate(
		modifier = modifier.padding(bottom = 20.dp),
		index = index,
		caption = item.name ?: "",
		thumbnailUrl = item.cover,
		onClick = { isChosen -> onItemPlateClick(isChosen, item) },
		isChosen = isChosen,
		width = 90.dp,
		height = 90.dp,
	)
}