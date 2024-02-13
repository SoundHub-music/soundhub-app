package com.soundhub.ui.postline.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.soundhub.ui.components.CircularAvatar
import com.soundhub.ui.components.buttons.LikeButton
import com.soundhub.ui.components.pagination.PagerPaginationDots
import com.soundhub.ui.gallery.ImageHorizontalPager
import com.soundhub.ui.theme.borderColor

@Composable
fun PostCard(
    modifier: Modifier = Modifier,
    postAuthor: String,
    publishDate: String,
    avatarUrl: String? = null,
    imageContent: List<String> = emptyList(),
    textContent: String,
    navController: NavHostController
) {
    Card(
        colors = CardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
            disabledContainerColor = Color.White,
            disabledContentColor = Color.Red
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
            postAuthor = postAuthor,
            publishDate = publishDate,
            avatarUrl = avatarUrl
        )

        PostImages(images = imageContent, navController = navController)
        PostContent(textContent = textContent)
    }
}

@Composable
private fun PostHeader(
    modifier: Modifier = Modifier,
    avatarUrl: String?,
    postAuthor: String,
    publishDate: String
) {
    // row with author avatar, author name and publish date
    Row(
        modifier = Modifier
            .padding(horizontal = 16.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        CircularAvatar(
            imageUrl = avatarUrl,
            contentDescription = postAuthor,
            modifier = Modifier
                .size(40.dp)
        )

        Column {
            Text(
                text = postAuthor,
                fontSize = 16.sp,
                lineHeight = 24.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 0.15.sp
            )
            Text(
                text = publishDate,
                fontSize = 16.sp,
                lineHeight = 20.sp,
                fontWeight = FontWeight.Medium,
                letterSpacing = 0.25.sp
            )
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun PostImages(
    modifier: Modifier = Modifier,
    images: List<String>,
    navController: NavHostController
) {
    val sliderState = rememberPagerState(initialPage = 0, pageCount = {images.size})

    Box(
        modifier = Modifier.fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(5.dp)) {
            ImageHorizontalPager(
                navController = navController,
                pagerState = sliderState,
                images = images,
            )
            PagerPaginationDots(sliderState = sliderState)
        }
    }
}

@Composable
private fun PostContent(textContent: String) {
    var isFavorite: Boolean by rememberSaveable { mutableStateOf(false) }
    Row(
        modifier = Modifier.padding(
            start = 16.dp, end = 16.dp,
            top = 10.dp, bottom = 10.dp
        )
    ) { Text(text = textContent) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(end = 8.dp, bottom = 8.dp),
        horizontalArrangement = Arrangement.End
    ) {
        LikeButton(isFavorite = isFavorite) { isFavorite = it }
    }
}

@Composable
@Preview(name = "PostCard")
fun PostCardPreview() {
    val navController = rememberNavController()

    PostCard(
        postAuthor = "Billie Eilish",
        publishDate = "20 minutes ago",
        textContent = "This is an example of PostCard",
        navController = navController
    )
}