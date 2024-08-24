package com.soundhub.ui.pages.music.components.tab_pages.library.components.dialog.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.soundhub.ui.pages.music.LastFmViewModel
import com.soundhub.ui.pages.music.SpotifyViewModel
import com.soundhub.ui.pages.music.VKViewModel
import com.soundhub.ui.pages.music.enums.ChosenMusicService
import com.soundhub.ui.viewmodels.MusicServiceDialogViewModel

@Composable
internal fun AuthorizedMusicServiceDialog(
	musicServiceDialogViewModel: MusicServiceDialogViewModel,
	updateDialogState: (Boolean) -> Unit,
) {
	val chosenMusicService: ChosenMusicService = musicServiceDialogViewModel
		.chosenMusicService

	Box(
		modifier = Modifier
			.padding(
				top = 15.dp,
				bottom = 30.dp,
				start = 20.dp,
				end = 20.dp
			)
			.fillMaxWidth()
	) {
		Column(
			horizontalAlignment = Alignment.CenterHorizontally,
			modifier = Modifier.fillMaxWidth()
		) {
			Box {
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
}