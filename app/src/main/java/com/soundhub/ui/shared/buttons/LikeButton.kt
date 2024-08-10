package com.soundhub.ui.shared.buttons

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.material.icons.rounded.FavoriteBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.IconToggleButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

@Composable
fun LikeButton(isFavorite: Boolean, onCheckedChange: (Boolean) -> Unit) {
	IconToggleButton(
		onCheckedChange = onCheckedChange,
		checked = isFavorite
	) {
		if (isFavorite)
			Icon(
				Icons.Rounded.Favorite,
				contentDescription = "like",
				tint = Color.Red
			)
		else Icon(
			Icons.Rounded.FavoriteBorder,
			contentDescription = "not like",
		)
	}
}