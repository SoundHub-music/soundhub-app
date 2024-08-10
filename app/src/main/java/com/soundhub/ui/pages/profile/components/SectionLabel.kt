package com.soundhub.ui.pages.profile.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
internal fun SectionLabel(
	modifier: Modifier = Modifier,
	labelIcon: Painter? = null,
	iconTint: Color? = null,
	contentDescription: String? = null,
	text: String,
) {
	Row(
		verticalAlignment = Alignment.CenterVertically,
		horizontalArrangement = Arrangement.spacedBy(space = 8.dp),
		modifier = Modifier.padding(horizontal = 8.dp)
	) {
		if (labelIcon != null && iconTint != null)
			Icon(
				painter = labelIcon,
				contentDescription = contentDescription,
				modifier = Modifier.size(32.dp),
				tint = iconTint
			)
		Text(
			modifier = modifier,
			text = text,
			color = MaterialTheme.colorScheme.onPrimaryContainer,
			fontWeight = FontWeight.Bold,
			fontSize = 16.sp
		)
	}
}