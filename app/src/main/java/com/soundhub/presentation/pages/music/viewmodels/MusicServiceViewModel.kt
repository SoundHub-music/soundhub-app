package com.soundhub.presentation.pages.music.viewmodels

import androidx.lifecycle.ViewModel
import com.soundhub.data.enums.ApiStatus
import com.soundhub.presentation.pages.music.enums.ChosenMusicService
import com.soundhub.presentation.viewmodels.IProfileViewModel
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update

sealed class MusicServiceEvent {
	object CloseModal : MusicServiceEvent()
}

data class MusicServiceLoginState(
	val userName: String = "",
	val password: String = ""
)

abstract class MusicServiceViewModel<T> : ViewModel(), IProfileViewModel<T> {
	protected val _loginState = MutableStateFlow(MusicServiceLoginState())
	val loginState = _loginState.asStateFlow()

	protected val _serviceLogoResourceState = MutableStateFlow<Int?>(null)

	protected val _eventChannel = Channel<MusicServiceEvent>()
	val eventChannel = _eventChannel.receiveAsFlow()

	protected val _isAuthorizedState = MutableStateFlow(false)
	val isAuthorizedState = _isAuthorizedState.asStateFlow()

	protected val _statusState = MutableStateFlow(ApiStatus.NOT_LAUNCHED)
	val statusState = _statusState.asStateFlow()

	abstract val chosenMusicService: ChosenMusicService

	override fun onCleared() {
		super.onCleared()
		_loginState.update { MusicServiceLoginState() }
	}

	fun resetFormState() {
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

	abstract fun isAuthorized(): Deferred<Boolean>
	abstract fun checkAuthority(user: T?): Deferred<Boolean>
	abstract fun login()
	abstract fun logout()
}