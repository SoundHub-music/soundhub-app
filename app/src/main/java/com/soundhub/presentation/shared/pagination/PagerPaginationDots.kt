package com.soundhub.presentation.shared.pagination

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PagerPaginationDots(modifier: Modifier = Modifier, sliderState: PagerState) {
	if (sliderState.pageCount > 1) Row(
		horizontalArrangement = Arrangement.Center,
		modifier = Modifier.fillMaxWidth()
	) {
		repeat(sliderState.pageCount) { index ->
			Box(
				modifier = modifier
					.width(10.dp)
					.height(10.dp)
					.padding(2.dp)
					.background(
						color = if (sliderState.currentPage == index)
							MaterialTheme.colorScheme.secondaryContainer
						else MaterialTheme.colorScheme.onBackground,
						shape = CircleShape
					),
			)
		}
	}
}