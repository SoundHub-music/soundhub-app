package com.soundhub.presentation.pages.music.widgets.sheet

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.EaseIn
import androidx.compose.animation.core.EaseOut
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.soundhub.R
import com.soundhub.data.enums.ApiStatus
import com.soundhub.presentation.pages.music.viewmodels.MusicServiceBottomSheetViewModel
import com.soundhub.presentation.pages.music.widgets.sheet.components.FormIcon
import com.soundhub.presentation.shared.loaders.CircleLoader

@Composable
internal fun LoginMusicServiceSheet(
	musicServiceViewModel: MusicServiceBottomSheetViewModel,
	formIcon: Painter?,
	formIconDescription: String?,
) {
	val context = LocalContext.current
	val loginState by musicServiceViewModel.loginState.collectAsState()
	val formStatus: ApiStatus by musicServiceViewModel.statusState.collectAsState()
	val chosenMusicService = musicServiceViewModel.chosenMusicService.serviceName

	var loginButtonText = remember(formStatus) {
		when (formStatus) {
			ApiStatus.SUCCESS -> context.getString(R.string.music_service_login_form_success)
			ApiStatus.ERROR -> context.getString(R.string.music_service_login_form_error)
			else -> context.getString(
				R.string.music_service_login_signin_button,
				chosenMusicService
			)
		}
	}

	val isButtonEnabled = !formStatus.isError()

	val buttonColor by animateColorAsState(
		targetValue = if (isButtonEnabled)
			MaterialTheme.colorScheme.primary
		else MaterialTheme.colorScheme.error,
		label = "login button color",
		animationSpec = tween(durationMillis = 300, easing = EaseOut)
	)

	val buttonContentColor by animateColorAsState(
		targetValue = if (isButtonEnabled)
			MaterialTheme.colorScheme.onPrimary
		else MaterialTheme.colorScheme.onError,
		label = "login button content color",
		animationSpec = tween(durationMillis = 300, easing = EaseIn)
	)

	Box(modifier = Modifier) {
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
			formIcon?.let {
				FormIcon(
					icon = formIcon,
					iconDescription = formIconDescription,
				)
			}

			Column(verticalArrangement = Arrangement.spacedBy(20.dp)) {
				Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
					OutlinedTextField(
						value = loginState.userName,
						onValueChange = musicServiceViewModel::setUserName,
						placeholder = {
							Text(
								stringResource(
									id = R.string.music_service_login_username_placeholder,
									chosenMusicService
								)
							)
						},
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
						onValueChange = musicServiceViewModel::setPassword,
						visualTransformation = PasswordVisualTransformation(),
						placeholder = {
							Text(
								stringResource(
									id = R.string.music_service_login_password_placeholder,
									chosenMusicService
								)
							)
						},
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
					onClick = musicServiceViewModel::login,
					modifier = Modifier
						.fillMaxWidth(),
					shape = RoundedCornerShape(5.dp),
					colors = ButtonDefaults.buttonColors(
						containerColor = buttonColor,
						disabledContainerColor = buttonColor,
						contentColor = buttonContentColor,
						disabledContentColor = buttonContentColor
					),
					enabled = isButtonEnabled,
				) {
					if (formStatus.isLoading()) {
						CircleLoader(modifier = Modifier.size(20.dp))
					} else Text(text = loginButtonText)
				}
			}
		}
	}
}