package com.soundhub.presentation.pages.music.widgets.sheet

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.unit.dp
import com.soundhub.data.enums.ApiStatus
import com.soundhub.presentation.pages.music.viewmodels.MusicServiceViewModel
import com.soundhub.presentation.pages.music.widgets.sheet.components.FormIcon
import com.soundhub.presentation.pages.music.widgets.sheet.components.MusicServiceForm

@Composable
internal fun LoginMusicServiceSheet(
	musicServiceViewModel: MusicServiceViewModel<*>,
	formIcon: Painter?,
	formIconDescription: String?,
) {
	val loginState by musicServiceViewModel.loginState.collectAsState()
	val formStatus: ApiStatus by musicServiceViewModel.statusState.collectAsState()
	val chosenMusicService = musicServiceViewModel.chosenMusicService.serviceName

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

			MusicServiceForm(
				loginState = loginState,
				musicServiceViewModel = musicServiceViewModel,
				chosenMusicService = chosenMusicService,
				formStatus = formStatus,
			)
		}
	}
}