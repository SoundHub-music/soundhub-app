package com.soundhub.ui.profile.components.sections.photos

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.soundhub.R

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun UserPhotoCarousel(photos: List<String>) {
    val listState = rememberLazyListState()

    Column() {
        Text(text = stringResource(id = R.string.profile_screen_photo_section_caption))
        LazyRow(state = listState) {
            items(photos.size) {
                GlideImage(
                    modifier = Modifier.fillMaxSize(0.25f),
                    model = photos[it],
                    contentDescription = null)
            }
        }
    }
}