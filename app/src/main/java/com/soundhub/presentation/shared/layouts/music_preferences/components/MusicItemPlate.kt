package com.soundhub.presentation.shared.layouts.music_preferences.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import coil3.size.Size
import com.soundhub.R
import kotlinx.coroutines.delay

@Composable
fun MusicItemPlate(
	modifier: Modifier = Modifier,
	index: Int,
	clickable: Boolean = true,
	caption: String,
	thumbnailUrl: String?,
	onClick: (Boolean) -> Unit = {},
	isChosen: Boolean = false,
	width: Dp = 72.dp,
	height: Dp = 72.dp
) {
	var isItemChosen by rememberSaveable { mutableStateOf(isChosen) }

	VisibilityAnimationWrapper(index = index) {
		ItemContent(
			modifier = modifier,
			clickable = clickable,
			caption = caption,
			thumbnailUrl = thumbnailUrl,
			onClick = onClick,
			isChosen = isItemChosen,
			width = width,
			height = height,
		)
	}
}

@Composable
private fun VisibilityAnimationWrapper(
	index: Int,
	content: @Composable () -> Unit = {}
) {
	var isVisible by rememberSaveable {
		mutableStateOf(false)
	}

	LaunchedEffect(Unit) {
		if (!isVisible) {
			delay(index * 10L)
			isVisible = true
		}
	}

	AnimatedVisibility(
		visible = isVisible,
		enter = fadeIn(animationSpec = tween(250, delayMillis = index * 30)) +
				slideInVertically(
					animationSpec = tween(250, delayMillis = index * 30),
					initialOffsetY = { it / 2 }
				),
		exit = fadeOut(animationSpec = tween(100)) +
				slideOutVertically(animationSpec = tween(100)),
		label = "plate_$index"
	) { content() }
}

@Composable
private fun ItemContent(
	modifier: Modifier = Modifier,
	clickable: Boolean = true,
	caption: String,
	thumbnailUrl: String?,
	onClick: (Boolean) -> Unit = {},
	isChosen: Boolean = false,
	width: Dp = 72.dp,
	height: Dp = 72.dp
) {
	val selectionColor = MaterialTheme.colorScheme.error
	val placeholder: Painter = painterResource(id = R.drawable.musical_note)

	var isItemChosen by rememberSaveable { mutableStateOf(isChosen) }

	val borderWidth by animateDpAsState(
		targetValue = if (isItemChosen) 4.dp else 0.dp,
		animationSpec = tween(durationMillis = 300), label = "b.width"
	)

	val borderColor by animateColorAsState(
		targetValue = if (isItemChosen) selectionColor else Color.Transparent,
		animationSpec = tween(durationMillis = 300), label = "b.color"
	)

	Column(
		modifier = modifier,
		verticalArrangement = Arrangement.spacedBy(5.dp, Alignment.CenterVertically),
		horizontalAlignment = Alignment.CenterHorizontally,
	) {
		ElevatedCard(
			modifier = Modifier
				.width(width)
				.height(height)
				.border(
					width = borderWidth,
					color = borderColor,
					shape = RoundedCornerShape(16.dp)
				),
			shape = RoundedCornerShape(16.dp),
			onClick = {
				if (clickable) {
					isItemChosen = !isItemChosen
					onClick(isItemChosen)
				}
			}
		) {
			AsyncImage(
				model = ImageRequest.Builder(LocalContext.current)
					.data(thumbnailUrl)
					.crossfade(true)
					.diskCacheKey(thumbnailUrl)
					.memoryCacheKey(thumbnailUrl)
					.size(Size(width.value.toInt(), height.value.toInt()))
					.build(),
				contentScale = ContentScale.Crop,
				modifier = Modifier.fillMaxSize(),
				contentDescription = null,
				placeholder = placeholder,
				fallback = placeholder,
				error = placeholder
			)
		}
		Text(
			text = caption,
			style = TextStyle(
				fontSize = 15.sp,
				textAlign = TextAlign.Center,
				color = MaterialTheme.colorScheme.onSecondaryContainer,
			)
		)
	}
}

@Preview
@Composable
private fun ItemPlatePreview() {
	MusicItemPlate(index = 1, caption = "Rap", thumbnailUrl = "")
}