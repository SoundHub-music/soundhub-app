package com.soundhub.presentation.shared.post_card

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
internal fun PostContent(modifier: Modifier = Modifier, textContent: String) {
	Row(
		modifier = modifier.padding(
			horizontal = 16.dp, vertical = 12.dp
		)
	) { Text(text = textContent) }
}