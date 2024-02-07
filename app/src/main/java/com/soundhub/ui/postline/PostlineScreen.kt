package com.soundhub.ui.postline

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.soundhub.data.model.Post
import com.soundhub.ui.components.ContentContainer
import com.soundhub.ui.postline.components.PostCard

@Composable
fun PostLineScreen(
    modifier: Modifier = Modifier,
    postLineViewModel: PostlineViewModel = hiltViewModel(),
) {
    val posts = listOf(
        Post(
            id = 1,
            postAuthor = "Billie Eilish",
            publishDate = "20 minutes ago",
            textContent = "sdfksofsopgsrgk",
            avatar = null,
            imageContent = null
        ),
        Post(
            id = 2,
            postAuthor = "Billie Eilish",
            publishDate = "20 minutes ago",
            textContent = "sdfksofsopgsrgk",
            avatar = null,
            imageContent = null
        ),
        Post(
            id = 3,
            postAuthor = "Billie Eilish",
            publishDate = "20 minutes ago",
            textContent = "sdfksofsopgsrgk",
            avatar = null,
            imageContent = null
        ),
        Post(
            id = 4,
            postAuthor = "Billie Eilish",
            publishDate = "20 minutes ago",
            textContent = "sdfksofsopgsrgk",
            avatar = null,
            imageContent = null
        ),
        Post(
            id = 5,
            postAuthor = "Billie Eilish",
            publishDate = "20 minutes ago",
            textContent = "sdfksofsopgsrgk",
            avatar = null,
            imageContent = null
        ),
        Post(
            id = 6,
            postAuthor = "Billie Eilish",
            publishDate = "20 minutes ago",
            textContent = "sdfksofsopgsrgk",
            avatar = null,
            imageContent = null
        )

    )

    ContentContainer {
        if (posts.isEmpty())
            Text(text = "Здесь все ещё пусто :(")
        else
            LazyColumn(
                modifier.fillMaxHeight(),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(items = posts, key = { it.id }) { post ->
                    PostCard(
                        postAuthor = post.postAuthor,
                        publishDate = post.publishDate,
                        textContent = post.textContent,
                        imageContent = post.imageContent,
                        avatarUrl = post.avatar
                    )
                }
            }
    }
}