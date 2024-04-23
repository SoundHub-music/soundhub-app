package com.soundhub.ui.components.avatar

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.bumptech.glide.integration.compose.CrossFade
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.bumptech.glide.integration.compose.placeholder
import com.soundhub.R

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun CircularAvatar(
    modifier: Modifier = Modifier,
    imageUrl: String? = null,
    contentDescription: String? = null
) {

    LaunchedEffect(key1 = imageUrl) {
        Log.d("CircularAvatar", imageUrl.toString())
    }

    Box(
        modifier = modifier
            .clip(CircleShape)
            .size(40.dp),
        contentAlignment = Alignment.Center
    ) {
        if (imageUrl == null)
            Image(
                painter = painterResource(id = R.drawable.circular_user),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
        else GlideImage(
            model = imageUrl,
            contentDescription = contentDescription,
            modifier = Modifier
                .clip(CircleShape)
                .fillMaxSize(),
            contentScale = ContentScale.Crop,
            failure = placeholder(R.drawable.circular_user),
            transition = CrossFade
        )
    }
}

@Composable
@Preview
fun CircularAvatarPreview() {
    CircularAvatar()
}