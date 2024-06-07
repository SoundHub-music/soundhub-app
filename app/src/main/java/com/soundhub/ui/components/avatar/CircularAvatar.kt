package com.soundhub.ui.components.avatar

import android.net.Uri
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.bumptech.glide.integration.compose.CrossFade
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.bumptech.glide.integration.compose.placeholder
import com.soundhub.R

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun CircularAvatar(
    modifier: Modifier = Modifier,
    avatarViewModel: AvatarViewModel = hiltViewModel(),
    imageUrl: Uri? = null,
    contentDescription: String? = null,
    onClick: () -> Unit = {}
) {
    val glideUrl by avatarViewModel.imageUrl.collectAsState()

    LaunchedEffect(imageUrl) {
        avatarViewModel.loadGlideUrlOrImageUri(imageUrl)
    }

    Box(
        modifier = modifier
            .clip(CircleShape)
            .size(40.dp),
        contentAlignment = Alignment.Center
    ) {
        GlideImage(
            model = glideUrl,
            contentDescription = contentDescription,
            modifier = Modifier
                .clip(CircleShape)
                .fillMaxSize()
                .clickable { onClick() },
            contentScale = ContentScale.Crop,
            failure = placeholder(R.drawable.circular_user),
            transition = CrossFade
        )
    }
}

@Composable
@Preview
private fun CircularAvatarPreview() {
    CircularAvatar()
}