package com.soundhub.presentation.pages.music.viewmodels

import androidx.lifecycle.ViewModel
import com.soundhub.data.enums.ApiStatus
import com.soundhub.domain.model.LastFmUser
import com.soundhub.presentation.pages.music.enums.ChosenMusicService
import com.soundhub.utils.constants.Constants
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

sealed class MusicServiceEvent {
	object CloseModal : MusicServiceEvent()
}

@HiltViewModel
open class MusicServiceBottomSheetViewModel @Inject constructor() : ViewModel() {
	protected val _loginState = MutableStateFlow(MusicServiceLoginState())
	val loginState = _loginState.asStateFlow()

	protected val _serviceLogoResourceState = MutableStateFlow<Int?>(null)
	val serviceLogoResourceState = _serviceLogoResourceState.asStateFlow()

	protected val _eventChannel = Channel<MusicServiceEvent>()
	val eventChannel = _eventChannel.receiveAsFlow()

	protected val mutableIsAuthorized = MutableStateFlow(false)
	val isAuthorizedState = mutableIsAuthorized.asStateFlow()

	protected val mutableStatus = MutableStateFlow(ApiStatus.NOT_LAUNCHED)
	val statusState = mutableStatus.asStateFlow()

	open val chosenMusicService: ChosenMusicService
		get() = throw NotImplementedError(Constants.PROPERTY_NOT_IMPLEMENTED_ERROR)

	fun resetState() {
		_loginState.update { MusicServiceLoginState() }
	}

	fun getServiceLogoResource(): Int? {
		return _serviceLogoResourceState.value
	}

	fun setUserName(value: String) = _loginState.update {
		it.copy(userName = value)
	}

	fun setPassword(value: String) = _loginState.update {
		it.copy(password = value)
	}

	open fun checkAuthority(user: LastFmUser?): Boolean {
		return false
	}

	open fun login() {}
}

data class MusicServiceLoginState(
	val userName: String = "",
	val password: String = ""
)
