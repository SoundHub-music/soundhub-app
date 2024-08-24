package com.soundhub.ui.pages.music.components.tab_pages.library.components.dialog.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.soundhub.R
import com.soundhub.data.enums.ApiStatus
import com.soundhub.ui.viewmodels.MusicServiceDialogViewModel

@Composable
internal fun UnauthorizedMusicServiceDialog(
	musicServiceDialogViewModel: MusicServiceDialogViewModel,
	formIcon: Painter?,
	formIconDescription: String?,
	loginButtonEnabledState: Boolean = true,
) {
	val context = LocalContext.current
	val loginState by musicServiceDialogViewModel.loginState.collectAsState()
	val formStatus: ApiStatus by musicServiceDialogViewModel.statusState.collectAsState()
	val chosenMusicService = musicServiceDialogViewModel.chosenMusicService.serviceName

	var loginButtonText by remember {
		mutableStateOf(
			context.getString(
				R.string.music_service_login_signin_button,
				chosenMusicService
			)
		)
	}

	LaunchedEffect(key1 = formStatus) {
		loginButtonText = when (formStatus) {
			ApiStatus.SUCCESS -> context.getString(R.string.music_service_login_form_success)
			ApiStatus.ERROR -> context.getString(R.string.music_service_login_form_error)
			else -> context.getString(
				R.string.music_service_login_signin_button,
				chosenMusicService
			)
		}
	}

	Box {
		Column(
			verticalArrangement = Arrangement.spacedBy(10.dp, Alignment.CenterVertically),
			horizontalAlignment = Alignment.CenterHorizontally,
			modifier = Modifier
				.padding(
					top = 20.dp,
					bottom = 30.dp,
					start = 15.dp,
					end = 15.dp
				)
		) {
			FormIcon(
				icon = formIcon,
				iconDescription = formIconDescription,
			)

			Column(verticalArrangement = Arrangement.spacedBy(20.dp)) {
				Column(
					verticalArrangement = Arrangement.spacedBy(10.dp)
				) {
					OutlinedTextField(
						value = loginState.userName,
						onValueChange = musicServiceDialogViewModel::setUserName,
						placeholder = { Text(
							stringResource(
								id = R.string.music_service_login_username_placeholder,
								chosenMusicService
							)
						) },
						leadingIcon = {
							Icon(
								painter = painterResource(id = R.drawable.rounded_badge_24),
								contentDescription = "username"
							)
						},
						modifier = Modifier.fillMaxWidth()
					)

					OutlinedTextField(
						value = loginState.password,
						onValueChange = musicServiceDialogViewModel::setPassword,
						placeholder = { Text(
							stringResource(
								id = R.string.music_service_login_password_placeholder,
								chosenMusicService
							)
						) },
						leadingIcon = {
							Icon(
								painter = painterResource(id = R.drawable.baseline_password_24),
								contentDescription = "password"
							)
						},
						modifier = Modifier.fillMaxWidth()
					)
				}

				Button(
					onClick = { musicServiceDialogViewModel.login() },
					modifier = Modifier.fillMaxWidth(),
					shape = RoundedCornerShape(5.dp),
					enabled = loginButtonEnabledState
				) { Text(text = loginButtonText) }
			}
		}
	}
}