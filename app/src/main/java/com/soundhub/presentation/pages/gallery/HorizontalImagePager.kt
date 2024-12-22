package com.soundhub.presentation.pages.gallery

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.soundhub.utils.enums.MediaFolder
import kotlin.math.absoluteValue

@OptIn(ExperimentalFoundationApi::class, ExperimentalGlideComposeApi::class)
@Composable
fun HorizontalImagePager(
	modifier: Modifier = Modifier,
	pagerState: PagerState,
	images: List<String>,
	navController: NavHostController? = null,
	contentScale: ContentScale = ContentScale.Crop,
	height: Dp = 300.dp,
	clickable: Boolean = true,
	mediaFolder: MediaFolder = MediaFolder.POST_PICTURE,
	pagerViewModel: HorizontalImagePagerViewModel = hiltViewModel()
) {
	HorizontalPager(
		state = pagerState,
		modifier = modifier
	) { page ->
		val pageOffset = (pagerState.currentPage - page) + pagerState.currentPageOffsetFraction
		val scaleFactor = 0.75f + (1f - 0.75f) * (1f - pageOffset.absoluteValue)

		Box(
			modifier = modifier
				.graphicsLayer {
					scaleX = scaleFactor
					scaleY = scaleFactor
				}
				.alpha(scaleFactor.coerceIn(0f, 1f))
				.clip(RoundedCornerShape(16.dp))
		) {
			GlideImage(
				model = pagerViewModel.getGlideUrlOrImageUri(
					images[page],
					mediaFolder
				),
				contentDescription = images[page],
				contentScale = contentScale,
				modifier = Modifier
					.fillMaxSize()
					.height(height)
					.clickable {
						pagerViewModel.onImageClick(
							clickable = clickable,
							navController = navController,
							images = images,
							page = page
						)
					}
					.alpha(pagerViewModel.getImageOpacity(pagerState, page)),
			)
		}
	}
}

