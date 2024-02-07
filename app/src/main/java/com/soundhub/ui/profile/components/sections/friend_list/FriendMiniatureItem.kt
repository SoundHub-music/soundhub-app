package com.soundhub.ui.profile.components.sections.friend_list

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun FriendMiniatureItem(avatarUrl: String? = null) {
    Box(
        modifier = Modifier
            .size(24.dp)
            .aspectRatio(1f)
            .background(color = Color.Gray, shape = CircleShape)
    ) {
        if (avatarUrl != null) {
            GlideImage(
                model = avatarUrl,
                contentDescription = "avatar",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
        }
    }
}


data class FriendItem(
    val avatarUrl: String?
)