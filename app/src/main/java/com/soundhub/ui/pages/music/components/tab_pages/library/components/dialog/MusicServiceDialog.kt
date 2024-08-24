package com.soundhub.ui.pages.music.components.tab_pages.library.components.dialog

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.soundhub.R
import com.soundhub.ui.pages.music.components.tab_pages.library.components.dialog.MusicDialogConfig.DIALOG_MIN_HEIGHT
import com.soundhub.ui.pages.music.components.tab_pages.library.components.dialog.components.AuthorizedMusicServiceDialog
import com.soundhub.ui.pages.music.components.tab_pages.library.components.dialog.components.UnauthorizedMusicServiceDialog
import com.soundhub.ui.viewmodels.MusicServiceDialogViewModel

@Composable
fun MusicServiceDialog(
	musicServiceDialogViewModel: MusicServiceDialogViewModel,
	formIcon: Painter?,
	formIconDescription: String? = null,
	updateDialogState: (Boolean) -> Unit
) {
	val isAuthorized by musicServiceDialogViewModel
		.isAuthorizedState.collectAsState()

	val headerColor by musicServiceDialogViewModel
		.headerFormColorState.collectAsState()

	Dialog(
		onDismissRequest = { updateDialogState(false) },
		properties = DialogProperties(usePlatformDefaultWidth = false)
	) {
		Box(
			contentAlignment = Alignment.Center,
			modifier = Modifier
				.padding(horizontal = 5.dp)
		) {
			Card(modifier = Modifier
				.fillMaxWidth()
				.defaultMinSize(minHeight = DIALOG_MIN_HEIGHT)
			) {
				Column {
					Row(
						horizontalArrangement = Arrangement.End,
						verticalAlignment = Alignment.CenterVertically,
						modifier = Modifier
							.fillMaxWidth()
							.background(
								headerColor?.let { colorResource(id = it) }
									?: Color.Transparent
							)
					) {
						IconButton(
							onClick = { updateDialogState(false) },
							modifier = Modifier.size(48.dp)
						) {
							Icon(
								painter = painterResource(id = R.drawable.round_close_24),
								contentDescription = "close window",
								modifier = Modifier.size(32.dp)
							)
						}
					}

					if (isAuthorized)
						AuthorizedMusicServiceDialog(
							musicServiceDialogViewModel = musicServiceDialogViewModel,
							updateDialogState = updateDialogState,
						)
					else UnauthorizedMusicServiceDialog(
						musicServiceDialogViewModel = musicServiceDialogViewModel,
						formIcon = formIcon,
						formIconDescription = formIconDescription,
					)
				}
			}
		}
	}
}
