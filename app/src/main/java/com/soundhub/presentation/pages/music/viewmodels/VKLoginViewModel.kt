package com.soundhub.presentation.pages.music.viewmodels

import com.soundhub.domain.states.IProfileUiState
import com.soundhub.presentation.pages.music.enums.ChosenMusicService
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

class VKLoginViewModel @Inject constructor(
	override val profileUiState: StateFlow<IProfileUiState<Nothing>>
) : MusicServiceViewModel<Nothing>() {
	override val chosenMusicService: ChosenMusicService
		get() = ChosenMusicService.VK

	override fun isAuthorized(): Deferred<Boolean> {
		TODO("Not yet implemented")
	}

	override fun checkAuthority(user: Nothing?): Deferred<Boolean> {
		TODO("Not yet implemented")
	}

	override fun login() {
		TODO("Not yet implemented")
	}

	override fun logout() {
		TODO("Not yet implemented")
	}

	override fun getUserName(): String {
		TODO("Not yet implemented")
	}

	override fun getAvatarModel(): Any? {
		TODO("Not yet implemented")
	}
}