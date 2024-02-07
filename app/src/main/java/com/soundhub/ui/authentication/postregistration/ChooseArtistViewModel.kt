package com.soundhub.ui.authentication.postregistration

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.soundhub.data.model.Artist
import com.soundhub.data.model.Genre
import com.soundhub.data.repository.MusicRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChooseArtistViewModel @Inject constructor(
    private val musicRepository: MusicRepository
): ViewModel() {
    var fetchedArtists = MutableStateFlow<List<Artist>>(emptyList())
        private set

    var chosenGenres = MutableStateFlow<List<Genre>>(emptyList())
        private set

    var isLoading = MutableStateFlow<Boolean>(false)
        private set

    init {
        viewModelScope.launch {
            var response = musicRepository.searchArtistByTags(chosenGenres.value.map { it.name ?: "" })
        }
    }
}