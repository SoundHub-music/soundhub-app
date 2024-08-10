package com.soundhub.ui.pages.chat.components.input_box

import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import com.soundhub.R

@Composable
internal fun EmojiButton() {
	IconButton(onClick = { /*TODO: implement emoji panel */ }) {
		Icon(
			painter = painterResource(id = R.drawable.baseline_emoji_emotions_24),
			contentDescription = "emoji",
			tint = colorResource(id = R.color.emoji_btn_dark)
		)
	}
}