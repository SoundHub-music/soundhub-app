package com.soundhub.presentation.shared.text

import androidx.compose.foundation.text.ClickableText
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextDecoration
import com.soundhub.R

const val URL_ANNOTATION_ID = "URL"

@Composable
fun TextLink(text: String) {
	val uriHandler = LocalUriHandler.current
	val annotatedLinkString = buildAnnotatedString {
		val start = 0
		val end = text.length

		append(text)
		addStyle(
			style = SpanStyle(
				textDecoration = TextDecoration.Underline,
				color = colorResource(id = R.color.link_color)
			),
			start = start,
			end = end
		)

		addStringAnnotation(
			tag = URL_ANNOTATION_ID,
			annotation = text,
			start = start,
			end = end
		)
	}

	ClickableText(
		text = annotatedLinkString,
		onClick = {
			annotatedLinkString.getStringAnnotations(URL_ANNOTATION_ID, it, it)
				.firstOrNull()?.let { link ->
					uriHandler.openUri(link.item)
				}
		}
	)
}