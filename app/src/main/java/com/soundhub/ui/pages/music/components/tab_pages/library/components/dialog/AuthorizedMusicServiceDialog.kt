package com.soundhub.ui.pages.music.components.tab_pages.library.components.dialog

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.soundhub.R
import com.soundhub.ui.pages.music.LastFmViewModel
import com.soundhub.ui.pages.music.SpotifyViewModel
import com.soundhub.ui.pages.music.VKViewModel
import com.soundhub.ui.pages.music.components.tab_pages.library.components.dialog.components.FormIcon
import com.soundhub.ui.pages.music.components.tab_pages.library.components.dialog.components.LastFmDetails
import com.soundhub.ui.pages.music.components.tab_pages.library.components.dialog.components.SpotifyDetails
import com.soundhub.ui.pages.music.components.tab_pages.library.components.dialog.components.VkDetails
import com.soundhub.ui.pages.music.enums.ChosenMusicService
import com.soundhub.ui.viewmodels.MusicServiceDialogViewModel

@Composable
internal fun AuthorizedMusicServiceDialog(
	musicServiceDialogViewModel: MusicServiceDialogViewModel,
	updateDialogState: (Boolean) -> Unit,
	formIcon: Painter?,
	formIconDescription: String?
) {
	val chosenMusicService: ChosenMusicService = musicServiceDialogViewModel
		.chosenMusicService

	Box {
		Row {
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

		Column(
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
				iconDescription = formIconDescription
			)

			when (chosenMusicService) {
				ChosenMusicService.LASTFM -> LastFmDetails(
					lastFmViewModel = musicServiceDialogViewModel as LastFmViewModel
				)

				ChosenMusicService.VK -> VkDetails(
					vkViewModel = musicServiceDialogViewModel as VKViewModel
				)

				ChosenMusicService.SPOTIFY -> SpotifyDetails(
					spotifyViewModel = musicServiceDialogViewModel as SpotifyViewModel
				)
			}
		}
	}
}