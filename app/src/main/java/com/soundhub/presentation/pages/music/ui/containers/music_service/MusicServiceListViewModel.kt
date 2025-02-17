package com.soundhub.presentation.pages.music.ui.containers.music_service

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import com.soundhub.R
import com.soundhub.presentation.pages.music.viewmodels.LastFmLoginViewModel
import com.soundhub.presentation.pages.music.viewmodels.MusicServiceBottomSheetViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

typealias MusicServicePair = Pair<Int, MusicServiceBottomSheetViewModel>

class MusicServiceListViewModel : ViewModel() {
	private val _clickedServicePair =
		MutableStateFlow<MusicServiceBottomSheetViewModel?>(null)

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
		_clickedServicePair.value?.resetState()
		setClickedServicePair(null)
	}

	@Composable
	fun getServicePairs(): List<MusicServicePair> {
		return listOf<MusicServicePair>(
			Pair(
				R.drawable.last_fm,
				hiltViewModel<LastFmLoginViewModel>()
			),
		)
	}
}