package com.soundhub.presentation.pages.settings

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.soundhub.presentation.pages.authentication.AuthenticationViewModel
import com.soundhub.presentation.pages.settings.ui.cards.UserCardSettings
import com.soundhub.presentation.pages.settings.ui.menu.SettingsMenu
import com.soundhub.presentation.shared.containers.ContentContainer

@Composable
fun SettingsScreen(
	authViewModel: AuthenticationViewModel,
) {
	ContentContainer(modifier = Modifier.padding(vertical = 10.dp)) {
		Column {
			UserCardSettings(authViewModel)
			HorizontalDivider(thickness = 1.dp)
			SettingsMenu()
		}
	}
}