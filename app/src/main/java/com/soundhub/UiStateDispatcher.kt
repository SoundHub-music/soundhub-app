package com.soundhub

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.soundhub.data.datastore.UserStore
import com.soundhub.utils.Route
import com.soundhub.utils.UiEvent
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
    var galleryUrls: List<String> = emptyList()
)

@HiltViewModel
class UiStateDispatcher @Inject constructor(
    private val userDataStore: UserStore
) : ViewModel() {
    private var _uiEvent = Channel<UiEvent>()
    val uiEvent: Flow<UiEvent> = _uiEvent.receiveAsFlow()

    var uiState = MutableStateFlow(UIState())
        private set

    init {
        viewModelScope.launch {
            userDataStore.getCreds().collect { creds ->
                // TODO: change id to session token
                if (creds.id != null) {
                    sendUiEvent(UiEvent.Navigate(Route.Postline))
                } else {
                    sendUiEvent(UiEvent.Navigate(Route.Authentication))
                }
            }
        }
    }

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
        it.copy(galleryUrls = value)
    }

    fun sendUiEvent(event: UiEvent) = viewModelScope.launch { _uiEvent.send(event) }
}