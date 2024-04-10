package com.soundhub.ui.postline.components.post_card

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.soundhub.R
import com.soundhub.data.model.Post
import com.soundhub.data.model.User
import com.soundhub.ui.components.buttons.LikeButton
import com.soundhub.ui.viewmodels.PostViewModel
import kotlinx.coroutines.launch

@Composable
internal fun PostBottomPanel(
    post: Post,
    user: User,
    postViewModel: PostViewModel
) {
    val coroutineScope = rememberCoroutineScope()
    var isFavorite: Boolean by rememberSaveable {
        mutableStateOf(postViewModel.isPostLiked(user, post))
    }

    LaunchedEffect(key1 = true) {
        Log.d("PostViewModel", post.likes.toString())
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        horizontalArrangement = Arrangement.End,
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (post.likes.isNotEmpty())
            Text(
                text = post.likes.size.toString(),
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily(Font(R.font.nunito_bold_italic))
            )
        LikeButton(isFavorite = isFavorite) {
            isFavorite = !isFavorite
            coroutineScope.launch {
                postViewModel.toggleLike(post.id)
            }
        }
    }
}