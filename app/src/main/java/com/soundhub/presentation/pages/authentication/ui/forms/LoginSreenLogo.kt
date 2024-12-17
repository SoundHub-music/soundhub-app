package com.soundhub.presentation.pages.authentication.ui.forms

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.soundhub.R

@Preview(name = "App name with logo")
@Composable
fun LoginScreenLogo(modifier: Modifier = Modifier) {
	val appLogo: Painter = painterResource(R.drawable.soundhub_logo)

	Row(
		modifier = modifier
			.fillMaxWidth()
			.padding(start = 16.dp, end = 16.dp, top = 30.dp, bottom = 30.dp),
		horizontalArrangement = Arrangement.spacedBy(
			space = 10.dp,
			alignment = Alignment.CenterHorizontally
		),
		verticalAlignment = Alignment.CenterVertically,
	) {
		Image(painter = appLogo, contentDescription = "SoundHub App logo")
		Text(
			text = stringResource(R.string.app_name),
			color = Color.White,
			fontWeight = FontWeight.ExtraBold,
			fontSize = 50.sp
		)
	}
}
