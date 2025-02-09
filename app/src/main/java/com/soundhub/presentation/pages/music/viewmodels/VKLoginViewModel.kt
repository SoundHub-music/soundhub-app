package com.soundhub.presentation.pages.music.viewmodels

import com.soundhub.presentation.pages.music.enums.ChosenMusicService
import javax.inject.Inject

class VKLoginViewModel @Inject constructor() : MusicServiceBottomSheetViewModel() {
	override val chosenMusicService: ChosenMusicService
		get() = ChosenMusicService.VK
}