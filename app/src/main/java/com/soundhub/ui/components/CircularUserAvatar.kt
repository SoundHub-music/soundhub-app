package com.soundhub.ui.components

import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.soundhub.ui.messenger.components.ChatItem

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun CircularUserAvatar(chatItem: ChatItem?) {
    if (chatItem?.userAvatarUrl == null)
        UserAvatarDefault(
            firstName = chatItem?.firstName ?: "",
            lastName = chatItem?.lastName ?: "",
            size = 40.dp
        )
    else
        GlideImage(
            model = chatItem.userAvatarUrl,
            contentDescription = "${chatItem.firstName}'s avatar",
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape),
            contentScale = ContentScale.Crop
        )
}