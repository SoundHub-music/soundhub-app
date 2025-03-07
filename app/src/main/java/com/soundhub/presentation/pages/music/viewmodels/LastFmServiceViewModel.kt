package com.soundhub.presentation.pages.music.viewmodels

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.soundhub.R
import com.soundhub.Route
import com.soundhub.data.enums.ApiStatus
import com.soundhub.data.local_database.dao.LastFmDao
import com.soundhub.domain.events.UiEvent
import com.soundhub.domain.model.LastFmUser
import com.soundhub.domain.repository.LastFmRepository
import com.soundhub.domain.states.MusicProfileUiState
import com.soundhub.domain.usecases.lastfm.LastFmAuthUseCase
import com.soundhub.presentation.pages.music.enums.ChosenMusicService
import com.soundhub.presentation.viewmodels.UiStateDispatcher
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LastFmServiceViewModel @Inject constructor(
	private val lastFmRepository: LastFmRepository,
	private val lastFmDao: LastFmDao,
	private val authUseCase: LastFmAuthUseCase,
	private val uiStateDispatcher: UiStateDispatcher,
) : MusicServiceViewModel<LastFmUser>() {
	private val _profileUiState = MutableStateFlow(MusicProfileUiState())
	override val profileUiState = _profileUiState.asStateFlow()

	override val chosenMusicService: ChosenMusicService
		get() = ChosenMusicService.LASTFM

	override fun onCleared() {
		super.onCleared()
		Log.d("LastFmViewModel", "ViewModel was cleared")
		resetFormState()
	}

	init {
		initLastFmUser()
		initServiceResourceIcon()
	}

	override fun getUserName(): String {
		val (profileOwner) = profileUiState.value
		return profileOwner?.name ?: ""
	}

	override fun getAvatarModel(): Any? {
		val (profileOwner) = profileUiState.value
		val avatar = profileOwner?.images?.find { it.size == "large" }

		return avatar?.url
	}

	override fun login() {
		viewModelScope.launch(Dispatchers.IO) {
			_statusState.update { ApiStatus.LOADING }
			val (userName, password) = _loginState.value
			val result = authUseCase.login(userName, password)

			result.onSuccess { response -> handleLoginSuccess(response.body) }
				.onFailure { handleLoginError() }
				.finally { handleLoginFinally() }
		}
	}

	private fun handleLoginSuccess(user: LastFmUser?) {
		_profileUiState.update { it.copy(user) }
		_statusState.update { ApiStatus.SUCCESS }
	}

	private fun handleLoginError() {
		_statusState.update { ApiStatus.ERROR }
	}

	private suspend fun handleLoginFinally() {
		delay(1000)

		if (statusState.value.isSuccess()) {
			_eventChannel.send(MusicServiceEvent.CloseModal)
			uiStateDispatcher.sendUiEvent(UiEvent.Navigate(Route.Music.LastFmProfile))
		}

		_statusState.update { ApiStatus.NOT_LAUNCHED }
	}

	override fun isAuthorized() = viewModelScope.async {
		authUseCase.isAuthorized()
	}

	override fun checkAuthority(user: LastFmUser?): Deferred<Boolean> {
		return viewModelScope.async { authUseCase.isAuthorized() }
	}

	override fun logout() {
		viewModelScope.launch(Dispatchers.IO) {
			authUseCase.logout()
			_profileUiState.update { it.copy(null) }
			uiStateDispatcher.sendUiEvent(UiEvent.PopBackStack)
		}
	}

	private fun initServiceResourceIcon() {
		_serviceLogoResourceState.update { R.drawable.last_fm }
	}

	private fun initLastFmUser() {
		viewModelScope.launch {
			val user: LastFmUser? = lastFmDao.getLastFmSessionUser()
			val isAuthorized: Deferred<Boolean> = checkAuthority(user)

			if (isAuthorized.await()) {
				_profileUiState.update { it.copy(user) }
				loadUserInfo()
			}
		}
	}

	private fun loadUserInfo() {
		viewModelScope.launch {
			val lastFmUser: LastFmUser? = lastFmDao.getLastFmSessionUser()

			if (lastFmUser != null) {
				_profileUiState.update {
					it.copy(profileOwner = lastFmUser)
					return@launch
				}
			}

			lastFmRepository.getUserInfo(lastFmUser?.name ?: "")
				.onSuccess { response ->
					Log.d("LastFmViewModel", response.body?.user.toString())
					_profileUiState.update { it.copy(response.body?.user) }
				}
				.onFailure { error ->
					Log.e(
						"LastFmViewModel",
						"getUserInfo[1]: ${error.throwable?.stackTraceToString()}"
					)
				}
		}
	}
}
