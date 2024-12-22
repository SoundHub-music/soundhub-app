package com.soundhub.presentation.shared.post_card

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.soundhub.presentation.pages.gallery.HorizontalImagePager
import com.soundhub.presentation.shared.pagination.PagerPaginationDots

@OptIn(ExperimentalFoundationApi::class)
@Composable
internal fun PostImages(
	modifier: Modifier = Modifier,
	images: List<String>,
	navController: NavHostController,
) {
	val sliderState = rememberPagerState(initialPage = 0, pageCount = { images.size })

	Box(
		modifier = modifier.fillMaxWidth(),
		contentAlignment = Alignment.Center
	) {
		Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
			HorizontalImagePager(
				navController = navController,
				pagerState = sliderState,
				images = images,
			)
			PagerPaginationDots(sliderState = sliderState)
		}
	}
}