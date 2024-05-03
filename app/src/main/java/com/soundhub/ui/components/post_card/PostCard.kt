package com.soundhub.ui.components.post_card

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.soundhub.data.model.Post
import com.soundhub.data.model.User
import com.soundhub.ui.theme.borderColor
import com.soundhub.ui.viewmodels.PostViewModel
import com.soundhub.ui.viewmodels.UiStateDispatcher

@Composable
fun PostCard(
    modifier: Modifier = Modifier,
    post: Post,
    navController: NavHostController,
    uiStateDispatcher: UiStateDispatcher,
    currentUser: User?,
    postViewModel: PostViewModel = hiltViewModel()
) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = MaterialTheme.colorScheme.onPrimaryContainer
        ),
        modifier = modifier
            .fillMaxWidth()
            .shadow(
                elevation = 6.dp,
                spotColor = Color(0x40000000),
                ambientColor = Color(0x40000000)
            ),
        border = BorderStroke(1.dp, borderColor)
    ) {
        PostHeader(
            postAuthor = post.author,
            publishDate = post.publishDate,
            avatarUrl = post.author?.avatarUrl,
            navController = navController,
            currentUser = currentUser,
            postViewModel = postViewModel,
            post = post
        )
        PostContent(textContent = post.content)
        PostImages(
            images = post.images ?: emptyList(),
            navController = navController,
            uiStateDispatcher = uiStateDispatcher
        )
        currentUser?.let {
            PostBottomPanel(
                post = post,
                user = it,
                postViewModel = postViewModel
            )
        }
    }
}