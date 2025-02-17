package com.soundhub.presentation.pages.music_profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.soundhub.data.local_database.dao.LastFmDao
import com.soundhub.domain.model.LastFmUser
import com.soundhub.domain.repository.LastFmRepository
import com.soundhub.domain.states.MusicProfileUiState
import com.soundhub.presentation.viewmodels.IProfileViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MusicProfileViewModel @Inject constructor(
	private val lastFmRepository: LastFmRepository,
	private val lastFmDao: LastFmDao
) : ViewModel(), IProfileViewModel<LastFmUser> {
	private val _profileUiState = MutableStateFlow(MusicProfileUiState())
	override val profileUiState: StateFlow<MusicProfileUiState> = _profileUiState.asStateFlow()

	init {
		loadUserInfo()
	}

	fun loadUserInfo() {
		viewModelScope.launch(Dispatchers.IO) {
			val sessionUser = lastFmDao.getLastFmSessionUser()

			if (sessionUser == null)
				return@launch

			lastFmRepository.getUserInfo(sessionUser.name)
				.onSuccess { userResponse ->
					val user: LastFmUser? = userResponse.body?.user

					if (user == null)
						return@onSuccess

					_profileUiState.update {
						it.copy(profileOwner = user)
					}
				}
		}
	}

	fun logout() {
		viewModelScope.launch {
			val user = _profileUiState.firstOrNull()?.profileOwner

			user?.let { lastFmDao.deleteLastFmSessionUser(user) }
		}
	}

	override fun getUserName(): String {
		val (profileOwner) = _profileUiState.value

		return profileOwner?.name ?: ""
	}

	override fun getAvatarModel(): Any? {
		val (profileOwner) = _profileUiState.value
		val avatar = profileOwner?.images?.find { it.size == "large" }

		return avatar?.url
	}
}