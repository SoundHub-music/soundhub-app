package com.soundhub.presentation.pages.music.widgets.sheet.components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
internal fun SummaryDetails(title: @Composable () -> Unit, content: @Composable () -> Unit) {
	Box(
		modifier = Modifier
			.defaultMinSize(minHeight = 40.dp, minWidth = 80.dp)
			.border(1.dp, Color.Gray)
	) {
		Column(modifier = Modifier.padding(10.dp)) {
			title()
			content()
		}
	}
}