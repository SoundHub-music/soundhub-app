package com.soundhub.ui.authentication.postregistration.components

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImagePainter
import coil.compose.SubcomposeAsyncImage
import coil.compose.SubcomposeAsyncImageContent
import coil.request.ImageRequest
import com.soundhub.R

@Composable
fun MusicItemPlate(
    modifier: Modifier = Modifier,
    caption: String,
    thumbnailUrl: String? = null,
    onClick: (Boolean) -> Unit = {},
    isChosen: Boolean = false,
    width: Dp = 72.dp,
    height: Dp = 72.dp
) {
    var isItemChosen by rememberSaveable { mutableStateOf(isChosen) }
    var itemBoxModifier: Modifier = Modifier
        .width(width)
        .height(height)
        .clip(shape = RoundedCornerShape(16.dp))
        .clickable {
            isItemChosen = !isItemChosen
            onClick(isItemChosen)
        }

    if (isItemChosen)
        itemBoxModifier = itemBoxModifier
            .border(
                width = 5.dp,
                color = MaterialTheme.colorScheme.primary,
                shape = RoundedCornerShape(16.dp)
            )

    Column (
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(5.dp, Alignment.CenterVertically),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Box (modifier = itemBoxModifier) {
            SubcomposeAsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(thumbnailUrl)
                    .crossfade(true)
                    .build(),
                contentDescription = caption,
                contentScale = ContentScale.Crop,
            ) {
                val state = painter.state
                if (state is AsyncImagePainter.State.Error || state is AsyncImagePainter.State.Loading)
                    SubcomposeAsyncImageContent(
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop,
                        painter = painterResource(id = R.drawable.musical_note)
                    )
            }
        }
        Text(
           text = caption,
            style = TextStyle(
                fontSize = 15.sp,
                textAlign = TextAlign.Center,
                fontFamily = FontFamily(Font(R.font.nunito_regular)),
                color = MaterialTheme.colorScheme.onSecondaryContainer,
            )
        )
    }
}

@Preview
@Composable
private fun ItemPlatePreview() {
    MusicItemPlate(caption = "Rap")
}