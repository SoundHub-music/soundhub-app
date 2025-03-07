package com.soundhub.presentation.pages.music.widgets.sheet.components

import android.content.Context
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.EaseIn
import androidx.compose.animation.core.EaseOut
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.soundhub.R
import com.soundhub.data.enums.ApiStatus
import com.soundhub.presentation.pages.music.viewmodels.MusicServiceLoginState
import com.soundhub.presentation.pages.music.viewmodels.MusicServiceViewModel
import com.soundhub.presentation.shared.loaders.CircleLoader

@Composable
internal fun MusicServiceForm(
	loginState: MusicServiceLoginState,
	musicServiceViewModel: MusicServiceViewModel<*>,
	chosenMusicService: String,
	formStatus: ApiStatus,
) {
	val context: Context = LocalContext.current
	val isButtonEnabled: Boolean = !formStatus.isError()

	var loginButtonText: String = remember(formStatus) {
		when (formStatus) {
			ApiStatus.SUCCESS -> context.getString(R.string.music_service_login_form_success)
			ApiStatus.ERROR -> context.getString(R.string.music_service_login_form_error)
			else -> context.getString(
				R.string.music_service_login_signin_button,
				chosenMusicService
			)
		}
	}

	val emailPlaceholder = stringResource(
		id = R.string.music_service_login_username_placeholder,
		chosenMusicService
	)

	val passwordPlaceholder = stringResource(
		id = R.string.music_service_login_password_placeholder,
		chosenMusicService
	)

	val buttonColor: Color by animateColorAsState(
		targetValue = if (isButtonEnabled)
			MaterialTheme.colorScheme.primary
		else MaterialTheme.colorScheme.error,
		label = "login button color",
		animationSpec = tween(durationMillis = 300, easing = EaseOut)
	)

	val buttonContentColor: Color by animateColorAsState(
		targetValue = if (isButtonEnabled)
			MaterialTheme.colorScheme.onPrimary
		else MaterialTheme.colorScheme.onError,
		label = "login button content color",
		animationSpec = tween(durationMillis = 300, easing = EaseIn)
	)

	Column(verticalArrangement = Arrangement.spacedBy(20.dp)) {
		Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
			OutlinedTextField(
				value = loginState.userName,
				modifier = Modifier.fillMaxWidth(),
				onValueChange = musicServiceViewModel::setUserName,
				placeholder = { Text(emailPlaceholder) },
				leadingIcon = {
					Icon(
						painter = painterResource(id = R.drawable.rounded_badge_24),
						contentDescription = "username"
					)
				},
			)

			OutlinedTextField(
				value = loginState.password,
				onValueChange = musicServiceViewModel::setPassword,
				visualTransformation = PasswordVisualTransformation(),
				placeholder = { Text(passwordPlaceholder) },
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
			modifier = Modifier.fillMaxWidth(),
			shape = RoundedCornerShape(5.dp),
			enabled = isButtonEnabled,
			colors = ButtonDefaults.buttonColors(
				containerColor = buttonColor,
				disabledContainerColor = buttonColor,
				contentColor = buttonContentColor,
				disabledContentColor = buttonContentColor
			)
		) {
			if (formStatus.isLoading()) {
				CircleLoader(modifier = Modifier.size(20.dp))
			} else Text(text = loginButtonText)
		}
	}
}