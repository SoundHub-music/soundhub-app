package com.soundhub.ui.gallery

import android.content.Context
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.soundhub.ui.viewmodels.UiStateDispatcher
import com.soundhub.Route
import com.soundhub.data.datastore.UserCredsStore
import com.soundhub.data.datastore.UserPreferences
import com.soundhub.utils.HttpUtils
import com.soundhub.utils.enums.MediaFolder
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlin.math.absoluteValue

@OptIn(ExperimentalFoundationApi::class, ExperimentalGlideComposeApi::class)
@Composable
fun ImageHorizontalPager(
    modifier: Modifier = Modifier,
    pagerState: PagerState,
    images: List<String>,
    navController: NavHostController? = null,
    uiStateDispatcher: UiStateDispatcher,
    contentScale: ContentScale = ContentScale.Crop,
    height: Dp = 300.dp,
    clickable: Boolean = true,
    mediaFolder: MediaFolder = MediaFolder.POST_PICTURE
) {
    val context: Context = LocalContext.current
    val userCredsFlow: Flow<UserPreferences> = UserCredsStore(context).getCreds()
    var userCreds: UserPreferences? by remember { mutableStateOf(null) }

    LaunchedEffect(key1 = userCredsFlow) {
        userCreds = userCredsFlow.firstOrNull()
    }

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
                model = HttpUtils.prepareGlideUrl(userCreds, images[page], mediaFolder),
                contentDescription = images[page],
                contentScale = contentScale,
                modifier = Modifier
                    .fillMaxSize()
                    .height(height)
                    .clickable {
                        onImageClick(
                            clickable = clickable,
                            uiStateDispatcher = uiStateDispatcher,
                            navController = navController,
                            images = images,
                            page = page
                        )
                    }
                    .alpha(getImageOpacity(pagerState, page)),
            )
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
private fun getImageOpacity(pagerState: PagerState, page: Int): Float {
    return if (pagerState.currentPage == page) 1f else 0.5f
}

private fun onImageClick(
    clickable: Boolean = false,
    uiStateDispatcher: UiStateDispatcher,
    navController: NavHostController?,
    images: List<String>,
    page: Int
) {
    if (clickable) {
        uiStateDispatcher.setGalleryUrls(images)
        navController?.navigate("${Route.Gallery.route}/${page}")
    }
}