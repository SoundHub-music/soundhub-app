package com.soundhub.ui.gallery

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import com.soundhub.ui.viewmodels.UiStateDispatcher

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun GalleryScreen(
    modifier: Modifier = Modifier,
    images: List<String> = emptyList(),
    initialPage: Int = 0,
    uiStateDispatcher: UiStateDispatcher
) {
    val pagerState = rememberPagerState(
        initialPage = initialPage,
        pageCount = { images.size }
    )

    ImageHorizontalPager(
        modifier = modifier.fillMaxSize(),
        pagerState = pagerState,
        images = images,
        contentScale = ContentScale.Fit,
        clickable = false,
        uiStateDispatcher = uiStateDispatcher
    )
}