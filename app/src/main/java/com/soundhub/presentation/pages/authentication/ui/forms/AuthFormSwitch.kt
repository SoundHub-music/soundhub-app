package com.soundhub.presentation.pages.authentication.ui.forms

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.soundhub.R

@Composable
fun AuthFormSwitch(isRegisterForm: Boolean, onCheckedChange: (Boolean) -> Unit) {
	Row(
		verticalAlignment = Alignment.CenterVertically,
		horizontalArrangement = Arrangement.spacedBy(10.dp)
	) {
		Switch(
			checked = isRegisterForm,
			onCheckedChange = onCheckedChange
		)
		Text(
			text = stringResource(R.string.get_account_switch_label),
			fontWeight = FontWeight.Bold
		)
	}
}