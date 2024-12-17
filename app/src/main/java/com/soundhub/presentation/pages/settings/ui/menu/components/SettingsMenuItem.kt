package com.soundhub.presentation.pages.settings.ui.menu.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun SettingsMenuItem(
	modifier: Modifier = Modifier,
	icon: Painter,
	iconContentDescription: String? = null,
	title: String,
	onClick: () -> Unit = {},
	actionElement: @Composable () -> Unit = {}
) {
	Row(
		modifier = modifier
			.fillMaxWidth()
			.clip(RoundedCornerShape(10.dp))
			.clickable { onClick() },
	) {
		Row(
			modifier = Modifier
				.padding(vertical = 12.dp, horizontal = 5.dp)
				.fillMaxWidth(),
			horizontalArrangement = Arrangement.SpaceBetween,
			verticalAlignment = Alignment.CenterVertically
		) {
			Row(
				horizontalArrangement = Arrangement.spacedBy(10.dp),
				verticalAlignment = Alignment.CenterVertically
			) {
				Image(
					painter = icon,
					contentDescription = iconContentDescription,
					modifier = Modifier.size(32.dp)
				)
				Text(
					text = title,
					fontWeight = FontWeight.SemiBold,
					fontSize = 16.sp,
					lineHeight = 32.sp
				)
			}
			actionElement()
		}
	}
}

data class SettingsMenuItemData(
	val icon: Int,
	val iconContentDescription: String? = null,
	val title: String,
	val onClick: () -> Unit = {},
	val actionElement: @Composable () -> Unit = {},
	val hasTopDivider: Boolean = false
)