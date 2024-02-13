package com.soundhub.ui.authentication.postregistration

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.soundhub.UiStateDispatcher
import com.soundhub.data.model.Artist
import com.soundhub.data.model.Genre
import com.soundhub.data.repository.MusicRepository
import com.soundhub.utils.UiEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PostRegistrationViewModel @Inject constructor(
    private val musicRepository: MusicRepository,
    private val uiStateDispatcher: UiStateDispatcher
): ViewModel() {
    var genres = MutableStateFlow<List<Genre>>(emptyList())
        private set
    var artists = MutableStateFlow<List<Artist>>(emptyList())
        private set
    var isLoading = MutableStateFlow(false)
        private set
    var chosenGenres = MutableStateFlow<List<Genre>>(emptyList())
        private set
    var chosenArtists = MutableStateFlow<List<Artist>>(emptyList())
        private set

    init {
        viewModelScope.launch {
            try {
                isLoading.value = true
                val response = musicRepository.getAllGenres()
                Log.d("genres", response.body()?.genres?.size.toString())

                if (response.isSuccessful)
                    genres.value = response.body()?.genres ?: emptyList()
            }
            catch (e: Exception) {
                uiStateDispatcher.sendUiEvent(UiEvent.ShowToast("Ошибка"))
            }
            finally {
                isLoading.value = false
            }
        }
    }

    fun setChosenGenres(list: List<Genre>) = chosenGenres.update { list }

    fun setChosenGenres(genre: Genre) = chosenGenres.update {
        it.toMutableList().apply { add(genre) }
    }

    fun setChosenArtists(list: List<Artist>) = chosenArtists.update { list }

    fun setChosenArtists(artist: Artist) = chosenArtists.update {
        it.toMutableList().apply { add(artist) }
    }
}