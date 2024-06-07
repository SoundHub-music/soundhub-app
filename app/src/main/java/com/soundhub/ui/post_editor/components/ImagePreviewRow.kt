package com.soundhub.ui.post_editor.components

import android.content.Context
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.bumptech.glide.load.model.GlideUrl
import com.soundhub.data.datastore.UserCredsStore
import com.soundhub.data.datastore.UserPreferences
import com.soundhub.ui.post_editor.PostEditorState
import com.soundhub.ui.post_editor.PostEditorViewModel
import com.soundhub.utils.HttpUtils
import com.soundhub.utils.enums.MediaFolder
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull

@Composable
internal fun ImagePreviewRow(
    postEditorViewModel: PostEditorViewModel
) {
    val postEditorState: PostEditorState by postEditorViewModel
        .postEditorState.collectAsState()

    val currentImages: List<String> = postEditorState.images
    val newImages: List<String> = postEditorState.newImages
    val allImages: List<String> = currentImages + newImages

    LazyRow(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(items = allImages) { uri ->
            ImageItem(imageUrl = uri, postEditorViewModel = postEditorViewModel)
        }
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
private fun ImageItem(
    modifier: Modifier = Modifier,
    postEditorViewModel: PostEditorViewModel,
    imageUrl: String
) {
    val context: Context = LocalContext.current
    val userCredsFlow: Flow<UserPreferences> = UserCredsStore(context).getCreds()
    var userCreds: UserPreferences? by remember {
        mutableStateOf(null)
    }
    val glideUrl: GlideUrl? = remember(userCreds) {
        HttpUtils.prepareGlideUrWithAccessToken(userCreds, imageUrl, MediaFolder.POST_PICTURE)
    }

    LaunchedEffect(key1 = userCredsFlow) {
        userCreds = userCredsFlow.firstOrNull()
    }

    Box(
        modifier = modifier,
        contentAlignment = Alignment.TopEnd
    ) {
        IconButton(
            onClick = { postEditorViewModel.deleteImage(imageUrl) },
            modifier = Modifier.zIndex(1f)
        ) {
            Icon(
                imageVector = Icons.Rounded.Close,
                contentDescription = "delete photo",
                modifier = Modifier
                    .size(20.dp)
                    .padding(0.dp)
            )
        }
        GlideImage(
            model = glideUrl,
            contentDescription = imageUrl,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .clip(RoundedCornerShape(12.dp))
                .width(100.dp)
                .height(100.dp)
        )
    }
}