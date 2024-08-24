package com.soundhub.ui.pages.music

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.soundhub.R
import com.soundhub.data.api.responses.lastfm.LastFmFullUser
import com.soundhub.data.dao.LastFmDao
import com.soundhub.data.enums.ApiStatus
import com.soundhub.data.model.LastFmUser
import com.soundhub.data.repository.LastFmRepository
import com.soundhub.ui.pages.music.enums.ChosenMusicService
import com.soundhub.ui.viewmodels.MusicServiceDialogViewModel
import com.soundhub.utils.mappers.LastFmMapper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LastFmViewModel @Inject constructor(
	private val lastFmRepository: LastFmRepository,
	private val lastFmDao: LastFmDao
) : MusicServiceDialogViewModel() {
	private val _lastFmUser = MutableStateFlow<LastFmUser?>(null)
	val lastFmUser = _lastFmUser.asStateFlow()

	private val _userInfo = MutableStateFlow<LastFmFullUser?>(null)
	val userInfo = _userInfo.asStateFlow()

	override val chosenMusicService: ChosenMusicService
		get() = ChosenMusicService.LASTFM

	init {
		initLastFmUser()
		initHeaderIcon()
	}

	override fun initHeaderIcon() = mutableHeaderFormColor.update { R.color.last_fm_color }

	private fun initLastFmUser() = viewModelScope.launch {
		val user: LastFmUser? = lastFmDao.getLastFmSessionUser()
		checkAuthority(user)
		_lastFmUser.update { user }
		getUserInfo()
	}

	private fun getUserInfo() = viewModelScope.launch {
		val lastFmUser = _lastFmUser.value
		lastFmRepository.getUserInfo(lastFmUser?.name ?: "")
			.onSuccess { response ->
				_userInfo.update { response.body?.user }
				Log.d("LastFmViewModel", response.body?.user.toString())
			}
			.onFailure { error ->
				Log.e("LastFmViewModel", "getUserInfo[1]: ${error.throwable?.stackTraceToString()}")
			}
	}

	override fun login() = viewModelScope.launch(Dispatchers.IO) {
		val (userName, password) = mutableLoginState.value
		mutableStatus.update { ApiStatus.LOADING }

		lastFmRepository.getMobileSession(userName, password)
			.onSuccess { response ->
				val user: LastFmUser? = LastFmMapper
					.impl.lastFmSessionBodyToLastFmUser(response.body?.session)

				user?.let { lastFmDao.saveLastFmSessionUser(user) }
				mutableStatus.update { ApiStatus.SUCCESS }
			}
			.onFailure { mutableStatus.update { ApiStatus.ERROR } }
	}

	override fun checkAuthority(user: LastFmUser?) = viewModelScope.launch {
		Log.d("LastFmViewModel", "Last fm user $user")
		mutableIsAuthorized.update { user?.sessionKey != null }
	}
}
