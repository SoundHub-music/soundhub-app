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
import androidx.navigation.NavHostController
import com.soundhub.data.model.Post
import com.soundhub.ui.components.containers.ContentContainer
import com.soundhub.ui.postline.components.PostCard

@Composable
fun PostLineScreen(
    modifier: Modifier = Modifier,
    postLineViewModel: PostlineViewModel = hiltViewModel(),
    navController: NavHostController
) {
    val posts = listOf(
        Post(
            id = 1,
            postAuthor = "Billie Eilish",
            publishDate = "20 minutes ago",
            textContent = "sdfksofsopgsrgk",
            avatar = null,
            imageContent = listOf(
                "https://images.unsplash.com/photo-1503023345310-bd7c1de61c7d?q=80&w=1000&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxzZWFyY2h8Mnx8aHVtYW58ZW58MHx8MHx8fDA%3D",
                "https://media.istockphoto.com/id/1322277517/photo/wild-grass-in-the-mountains-at-sunset.jpg?s=612x612&w=0&k=20&c=6mItwwFFGqKNKEAzv0mv6TaxhLN3zSE43bWmFN--J5w="
            )
        ),
        Post(
            id = 2,
            postAuthor = "Billie Eilish",
            publishDate = "20 minutes ago",
            textContent = "sdfksofsopgsrgk",
            avatar = null,
            imageContent = listOf("https://img.freepik.com/free-photo/painting-mountain-lake-with-mountain-background_188544-9126.jpg")
        ),
        Post(
            id = 3,
            postAuthor = "Billie Eilish",
            publishDate = "20 minutes ago",
            textContent = "sdfksofsopgsrgk",
            avatar = null,
            imageContent = emptyList()
        ),
        Post(
            id = 4,
            postAuthor = "Billie Eilish",
            publishDate = "20 minutes ago",
            textContent = "sdfksofsopgsrgk",
            avatar = null,
            imageContent = emptyList()
        ),
        Post(
            id = 5,
            postAuthor = "Billie Eilish",
            publishDate = "20 minutes ago",
            textContent = "sdfksofsopgsrgk",
            avatar = null,
            imageContent = emptyList()
        ),
        Post(
            id = 6,
            postAuthor = "Billie Eilish",
            publishDate = "20 minutes ago",
            textContent = "sdfksofsopgsrgk",
            avatar = null,
            imageContent = emptyList()
        )

    )

    ContentContainer {
        if (posts.isEmpty())
            Text(text = "Здесь все ещё пусто :(")
        else LazyColumn(
            modifier.fillMaxHeight(),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(items = posts, key = { it.id }) { post ->
                PostCard(
                    postAuthor = post.postAuthor,
                    publishDate = post.publishDate,
                    textContent = post.textContent,
                    imageContent = post.imageContent ?: emptyList(),
                    avatarUrl = post.avatar,
                    navController = navController
                )
            }
        }
    }
}