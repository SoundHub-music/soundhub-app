package com.soundhub.ui.viewmodels

import androidx.lifecycle.ViewModel
import com.soundhub.data.enums.ApiStatus
import com.soundhub.data.model.LastFmUser
import com.soundhub.ui.pages.music.enums.ChosenMusicService
import com.soundhub.utils.constants.Constants
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
open class MusicServiceDialogViewModel @Inject constructor() : ViewModel() {
	protected val mutableLoginState = MutableStateFlow(MusicServiceLoginState())
	val loginState = mutableLoginState.asStateFlow()

	protected val mutableIsAuthorized = MutableStateFlow(false)
	val isAuthorizedState = mutableIsAuthorized.asStateFlow()

	protected val mutableStatus = MutableStateFlow(ApiStatus.NOT_LAUNCHED)
	val statusState = mutableStatus.asStateFlow()

	protected val mutableHeaderFormColor = MutableStateFlow<Int?>(null)
	val headerFormColorState = mutableHeaderFormColor.asStateFlow()

	open val chosenMusicService: ChosenMusicService
		get() = throw NotImplementedError(Constants.PROPERTY_NOT_IMPLEMENTED_ERROR)

	fun setUserName(value: String) = mutableLoginState.update {
		it.copy(userName = value)
	}

	fun setPassword(value: String) = mutableLoginState.update {
		it.copy(password = value)
	}

	open fun checkAuthority(user: LastFmUser?): Job {
		throw NotImplementedError(Constants.METHOD_NOT_IMPLEMENTED_ERROR)
	}

	open fun login(): Job {
		throw NotImplementedError(Constants.METHOD_NOT_IMPLEMENTED_ERROR)
	}

	protected open fun initHeaderIcon() {}
}

data class MusicServiceLoginState(
	val userName: String = "",
	val password: String = ""
)
