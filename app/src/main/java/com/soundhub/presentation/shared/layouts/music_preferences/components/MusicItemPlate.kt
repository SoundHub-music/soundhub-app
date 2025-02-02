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
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.soundhub.R

@Composable
fun MusicItemPlate(
	modifier: Modifier = Modifier,
	clickable: Boolean = true,
	caption: String,
	thumbnailUrl: String?,
	onClick: (Boolean) -> Unit = {},
	isChosen: Boolean = false,
	width: Dp = 72.dp,
	height: Dp = 72.dp
) {
	val tertiaryColor = MaterialTheme.colorScheme.tertiary
	val placeholder: Painter = painterResource(id = R.drawable.musical_note)

	var isItemChosen by rememberSaveable { mutableStateOf(isChosen) }

	val borderWidth by animateDpAsState(
		targetValue = if (isItemChosen) 4.dp else 0.dp,
		animationSpec = tween(durationMillis = 300), label = "b.width"
	)

	val borderColor by animateColorAsState(
		targetValue = if (isItemChosen) tertiaryColor else Color.Transparent,
		animationSpec = tween(durationMillis = 300), label = "b.color"
	)

	var isVisible by remember { mutableStateOf(false) }

	LaunchedEffect(true) {
		isVisible = true
	}

	AnimatedVisibility(
		visible = isVisible,
		enter = fadeIn() + slideInVertically(),
		exit = fadeOut() + slideOutVertically()
	) {
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
					model = thumbnailUrl,
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
}

@Preview
@Composable
private fun ItemPlatePreview() {
	MusicItemPlate(caption = "Rap", thumbnailUrl = "")
}