package com.soundhub.ui.pages.music

import com.soundhub.ui.pages.music.enums.ChosenMusicService
import com.soundhub.ui.viewmodels.MusicServiceDialogViewModel
import javax.inject.Inject

class SpotifyViewModel @Inject constructor() : MusicServiceDialogViewModel() {
	override val chosenMusicService: ChosenMusicService
		get() = ChosenMusicService.SPOTIFY
}