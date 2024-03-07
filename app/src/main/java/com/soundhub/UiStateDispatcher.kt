package com.soundhub

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class UIState(
    var isSearchBarActive: Boolean = false,
    var searchBarText: String = "",
    var galleryImageUrls: List<String> = emptyList(),
    var isLoading: Boolean = false
)

@HiltViewModel
class UiStateDispatcher @Inject constructor(
//    private val userDataStore: UserStore
) : ViewModel() {
    private var _uiEvent = Channel<UiEvent>()
    val uiEvent: Flow<UiEvent> = _uiEvent.receiveAsFlow()

    var uiState = MutableStateFlow(UIState())
        private set

    fun clearState() = uiState.update { UIState() }

    fun toggleSearchBarActive() = uiState.update {
        it.copy(isSearchBarActive = !it.isSearchBarActive, searchBarText = "")
    }

    fun setSearchBarActive(value: Boolean) = uiState.update {
        it.copy(isSearchBarActive = value, searchBarText = "")
    }

    fun updateSearchBarText(value: String) = uiState.update {
        it.copy(searchBarText = value)
    }

    fun setGalleryUrls(value: List<String>) = uiState.update {
        it.copy(galleryImageUrls = value)
    }

    fun setLoading(value: Boolean) = uiState.update { it.copy(isLoading = value) }

    fun sendUiEvent(event: UiEvent) = viewModelScope.launch { _uiEvent.send(event) }
}