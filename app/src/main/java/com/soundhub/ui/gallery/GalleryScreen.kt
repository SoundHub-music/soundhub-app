package com.soundhub.ui.gallery

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
) {
    val pagerState = rememberPagerState(
        initialPage = 0,
        pageCount = { images.size }
    )

    ImageHorizontalPager(
        modifier = modifier.fillMaxSize(),
        pagerState = pagerState,
        images = images,
        contentScale = ContentScale.Fit,
        clickable = false
    )
}