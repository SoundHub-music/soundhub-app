package com.soundhub.ui.authentication.postregistration

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.soundhub.data.model.Genre
import com.soundhub.data.repository.MusicRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChooseGenreViewModel @Inject constructor(
    private val musicRepository: MusicRepository
): ViewModel() {
    var genres = MutableStateFlow<List<Genre>>(emptyList())
        private set

    var isLoading = MutableStateFlow<Boolean>(false)
        private set

    init {
        viewModelScope.launch {
            isLoading.value = true
            val response = musicRepository.getAllGenres()
            Log.d("genres", response.body()?.genres?.size.toString())

            if (response.isSuccessful)
                genres.value = response.body()?.genres ?: emptyList()
            isLoading.value = false
        }
    }
}