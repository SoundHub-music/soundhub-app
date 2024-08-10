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
	protected val _loginState = MutableStateFlow(MusicServiceLoginState())
	val loginState = _loginState.asStateFlow()

	protected val _isAuthorized = MutableStateFlow(false)
	val isAuthorized = _isAuthorized.asStateFlow()

	protected val status = MutableStateFlow(ApiStatus.NOT_LAUNCHED)
	val statusState = status.asStateFlow()

	open val chosenMusicService: ChosenMusicService
		get() = throw NotImplementedError(Constants.PROPERTY_NOT_IMPLEMENTED_ERROR)

	fun setUserName(value: String) = _loginState.update {
		it.copy(userName = value)
	}

	fun setPassword(value: String) = _loginState.update {
		it.copy(password = value)
	}

	open fun checkAuthority(user: LastFmUser?): Job {
		throw NotImplementedError(Constants.METHOD_NOT_IMPLEMENTED_ERROR)
	}

	open fun login(): Job {
		throw NotImplementedError(Constants.METHOD_NOT_IMPLEMENTED_ERROR)
	}
}

data class MusicServiceLoginState(
	val userName: String = "",
	val password: String = ""
)
