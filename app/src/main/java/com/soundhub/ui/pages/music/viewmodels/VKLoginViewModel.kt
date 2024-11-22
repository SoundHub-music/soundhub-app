package com.soundhub.ui.pages.music.viewmodels

import com.soundhub.ui.pages.music.enums.ChosenMusicService
import javax.inject.Inject

class VKLoginViewModel @Inject constructor() : MusicServiceDialogViewModel() {
	override val chosenMusicService: ChosenMusicService
		get() = ChosenMusicService.VK
}