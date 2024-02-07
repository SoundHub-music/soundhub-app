package com.soundhub.ui.postline.components

import androidx.compose.material3.Icon
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.material.icons.rounded.FavoriteBorder
import androidx.compose.material3.Card
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage


@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun PostCard(
    modifier: Modifier = Modifier,
    postAuthor: String,
    publishDate: String,
    avatarUrl: String? = null,
    imageContent: List<String>? = null,
    textContent: String
) {
    var isFavorite: Boolean by remember { mutableStateOf(false) }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .shadow(
                elevation = 6.dp,
                spotColor = Color(0x40000000),
                ambientColor = Color(0x40000000)
            )
    ) {
        // row with author avatar, author name and publish date
        Row(
            modifier = Modifier
                .padding(start = 16.dp, end = 16.dp, top = 12.dp, bottom = 12.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            if (avatarUrl == null)
                AvatarSkeleton()
            else
                GlideImage(
                    model = avatarUrl,
                    contentDescription = postAuthor,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
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

        if (imageContent != null)
            Row(
                modifier = Modifier.height(300.dp)
            ) {
                /* TODO: implement image loading */
            }

        Row(
            modifier = Modifier.padding(
                start = 16.dp, end = 16.dp,
                top = 10.dp, bottom = 10.dp
            )
        ) { Text(text = textContent) }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp),
            horizontalArrangement = Arrangement.End
        ) {
            IconButton(
                onClick = { isFavorite = !isFavorite }) {
                if (isFavorite)
                    Icon(
                        Icons.Rounded.Favorite,
                        contentDescription = "like",
                        tint = Color.Red
                    )
                else Icon(
                    Icons.Rounded.FavoriteBorder,
                    contentDescription = "not like",
                    tint = Color.Red
                )
            }
        }
    }
}

@Composable
private fun AvatarSkeleton() {
    Box(
        modifier = Modifier
            .size(40.dp)
            .background(Color.Gray, CircleShape)
    ) {}
}


@Composable
@Preview(name = "AvatarSkeleton")
fun AvatarSkeletonPreview() { AvatarSkeleton() }

@Composable
@Preview(name = "PostCard")
fun PostCardPreview() {
    PostCard(
        postAuthor = "Billie Eilish",
        publishDate = "20 minutes ago",
        textContent = "This is an example of PostCard",
    )
}