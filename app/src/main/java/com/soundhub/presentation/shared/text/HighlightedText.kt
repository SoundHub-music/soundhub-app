package com.soundhub.presentation.shared.text

import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle

@Composable
fun HighlightedText(
	text: String,
	highlightedText: String,
	style: TextStyle = LocalTextStyle.current,
	highlightColor: Color = MaterialTheme.colorScheme.tertiary
) {
	Text(
		text = buildAnnotatedString {
			withStyle(
				style = SpanStyle(
					fontWeight = FontWeight.Bold,
					color = highlightColor
				)
			) {
				append(highlightedText)
			}
			append(" $text")
		},
		style = style
	)
}