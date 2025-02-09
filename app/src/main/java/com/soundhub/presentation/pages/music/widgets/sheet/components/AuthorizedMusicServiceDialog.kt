package com.soundhub.presentation.pages.music.widgets.sheet.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.soundhub.presentation.pages.music.enums.ChosenMusicService
import com.soundhub.presentation.pages.music.viewmodels.LastFmLoginViewModel
import com.soundhub.presentation.pages.music.viewmodels.MusicServiceBottomSheetViewModel
import com.soundhub.presentation.pages.music.viewmodels.VKLoginViewModel
import com.soundhub.presentation.pages.music.widgets.sheet.service_details.LastFmDetails
import com.soundhub.presentation.pages.music.widgets.sheet.service_details.VkDetails

@Composable
internal fun AuthorizedMusicServiceDialog(
	musicServiceBottomSheetViewModel: MusicServiceBottomSheetViewModel,
	updateDialogState: (Boolean) -> Unit,
) {
	val chosenMusicService: ChosenMusicService = musicServiceBottomSheetViewModel
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
						lastFmLoginViewModel = musicServiceBottomSheetViewModel as LastFmLoginViewModel
					)

					ChosenMusicService.VK -> VkDetails(
						vkLoginViewModel = musicServiceBottomSheetViewModel as VKLoginViewModel
					)
				}
			}
		}
	}
}