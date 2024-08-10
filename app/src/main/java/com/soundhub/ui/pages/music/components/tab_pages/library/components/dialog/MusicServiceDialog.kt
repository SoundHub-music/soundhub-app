package com.soundhub.ui.pages.music.components.tab_pages.library.components.dialog

import android.util.Log
import androidx.compose.material3.Card
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.window.Dialog
import com.soundhub.ui.viewmodels.MusicServiceDialogViewModel

@Composable
fun MusicServiceDialog(
	musicServiceDialogViewModel: MusicServiceDialogViewModel,
	formIcon: Painter?,
	formIconDescription: String?,
	onLoginClick: () -> Unit,
	userNamePlaceholder: String,
	passwordPlaceholder: String,
	loginButtonContent: @Composable () -> Unit,
	updateDialogState: (Boolean) -> Unit
) {
	val isAuthorized by musicServiceDialogViewModel.isAuthorized.collectAsState()

	LaunchedEffect(key1 = isAuthorized) {
		Log.d("LastFmViewModel", isAuthorized.toString())
	}

	Dialog(onDismissRequest = { updateDialogState(false) }) {
		Card {
			if (isAuthorized)
				AuthorizedMusicServiceDialog(
					musicServiceDialogViewModel = musicServiceDialogViewModel,
					updateDialogState = updateDialogState,
					formIcon = formIcon,
					formIconDescription = formIconDescription
				)
			else UnauthorizedMusicServiceDialog(
				musicServiceDialogViewModel = musicServiceDialogViewModel,
				formIcon = formIcon,
				formIconDescription = formIconDescription,
				onLoginClick = onLoginClick,
				userNamePlaceholder = userNamePlaceholder,
				passwordPlaceholder = passwordPlaceholder,
				loginButtonContent = loginButtonContent,
				updateDialogState = updateDialogState
			)
		}
	}
}
