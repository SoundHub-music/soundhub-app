package com.soundhub.ui.pages.chat.components.message_box_list

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.soundhub.utils.lib.DateFormatter
import java.time.LocalDate

@Composable
internal fun MessageDateChip(date: LocalDate) {
	Box(
		modifier = Modifier
			.padding(top = 5.dp)
			.fillMaxWidth()
			.zIndex(1F),
		contentAlignment = Alignment.TopCenter
	) {
		Row(
			modifier = Modifier
				.background(
					color = MaterialTheme.colorScheme.tertiaryContainer,
					shape = RoundedCornerShape(5.dp)
				),
		) {
			Text(
				modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp),
				text = DateFormatter.getStringDate(date),
			)
		}
	}
}