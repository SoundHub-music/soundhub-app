package com.soundhub.ui.components.avatar

import android.content.Context
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.bumptech.glide.integration.compose.CrossFade
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.bumptech.glide.integration.compose.placeholder
import com.soundhub.R
import com.soundhub.data.datastore.UserCredsStore
import com.soundhub.data.datastore.UserPreferences
import com.soundhub.utils.HttpUtils
import com.soundhub.utils.MedialFolder
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun CircularAvatar(
    modifier: Modifier = Modifier,
    imageUrl: String? = null,
    contentDescription: String? = null,
    onClick: () -> Unit = {}
) {
    val context: Context = LocalContext.current
    val userCredsFlow: Flow<UserPreferences> = UserCredsStore(context).getCreds()
    var userCreds: UserPreferences? by remember { mutableStateOf(null) }

    LaunchedEffect(key1 = userCredsFlow) {
        userCreds = userCredsFlow.firstOrNull()
    }

    Box(
        modifier = modifier
            .clip(CircleShape)
            .size(40.dp),
        contentAlignment = Alignment.Center
    ) {
        GlideImage(
            model = HttpUtils.prepareGlideUrl(userCreds, imageUrl, MedialFolder.AVATAR),
            contentDescription = contentDescription,
            modifier = Modifier
                .clip(CircleShape)
                .fillMaxSize()
                .clickable { onClick() },
            contentScale = ContentScale.Crop,
            failure = placeholder(R.drawable.circular_user),
            transition = CrossFade
        ) {
            it.thumbnail(HttpUtils.prepareGlideRequestBuilder(context, imageUrl))
        }
    }
}

@Composable
@Preview
private fun CircularAvatarPreview() {
    CircularAvatar()
}