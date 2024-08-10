package com.soundhub.ui.pages.music.components.tab_pages.library.components.dialog.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.unit.dp

@Composable
internal fun FormIcon(
	modifier: Modifier = Modifier,
	icon: Painter? = null,
	iconDescription: String?,
) {
	if (icon != null)
		Image(
			painter = icon,
			contentDescription = iconDescription,
			modifier = modifier
				.size(128.dp)
				.padding(10.dp)
		)
}