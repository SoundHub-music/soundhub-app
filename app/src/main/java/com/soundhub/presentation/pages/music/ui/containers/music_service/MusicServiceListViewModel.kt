package com.soundhub.presentation.pages.music.ui.containers.music_service

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import com.soundhub.R
import com.soundhub.Route
import com.soundhub.presentation.pages.music.viewmodels.LastFmServiceViewModel
import com.soundhub.presentation.pages.music.viewmodels.MusicServiceViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

typealias MusicServicePair = Pair<Int, MusicServiceViewModel<*>>

class MusicServiceListViewModel : ViewModel() {
	private val _clickedServicePair =
		MutableStateFlow<MusicServiceViewModel<*>?>(null)

	val clickedServicePair = _clickedServicePair.asStateFlow()

	private val _showModalSheet = MutableStateFlow<Boolean>(false)
	val showModalSheet = _showModalSheet.asStateFlow()

	override fun onCleared() {
		super.onCleared()
		_clickedServicePair.update { null }
	}

	fun setClickedServicePair(pair: MusicServicePair?) {
		_clickedServicePair.value = pair?.second
	}

	fun setShowModalSheet(value: Boolean) {
		_showModalSheet.update { value }
	}

	fun onDismissSheet() {
		_showModalSheet.update { false }
		_clickedServicePair.value?.resetFormState()
		setClickedServicePair(null)
	}

	fun onServiceClick(
		clickedServicePair: MusicServicePair,
		navController: NavHostController
	) {
		viewModelScope.launch {
			val isAuthorized = clickedServicePair.second.isAuthorized().await()

			if (!isAuthorized) {
				setShowModalSheet(true)
				setClickedServicePair(clickedServicePair)
				return@launch
			}

			navController.navigate(Route.Music.LastFmProfile.route)
		}
	}

	@Composable
	fun getServicePairs(): List<MusicServicePair> {
		return listOf<MusicServicePair>(
			Pair(
				R.drawable.last_fm,
				hiltViewModel<LastFmServiceViewModel>()
			),
		)
	}
}