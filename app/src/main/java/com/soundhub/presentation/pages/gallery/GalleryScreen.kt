package com.soundhub.presentation.pages.gallery

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun GalleryScreen(
	modifier: Modifier = Modifier,
	images: List<String> = emptyList(),
	initialPage: Int = 0,
	subFolder: String? = null
) {
	val pagerState = rememberPagerState(
		initialPage = initialPage,
		pageCount = { images.size }
	)

	HorizontalImagePager(
		modifier = modifier.fillMaxSize(),
		pagerState = pagerState,
		images = images,
		contentScale = ContentScale.Fit,
		clickable = false,
		subFolder = subFolder
	)
}