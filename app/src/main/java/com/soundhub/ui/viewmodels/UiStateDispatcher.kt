package com.soundhub.ui.viewmodels

import androidx.lifecycle.ViewModel
import com.soundhub.data.model.User
import com.soundhub.ui.events.UiEvent
import com.soundhub.ui.states.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject


@HiltViewModel
class UiStateDispatcher @Inject constructor() : ViewModel() {
    val uiEvent = MutableSharedFlow<UiEvent>()
    val uiState = MutableStateFlow(UiState())

    fun clearState() = uiState.update { UiState() }

    fun setCurrentRoute(route: String?) = uiState.update {
        it.copy(currentRoute = route)
    }

    fun setAuthorizedUser(user: User?) = uiState.update {
        it.copy(authorizedUser = user)
    }

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

    suspend fun sendUiEvent(event: UiEvent) = uiEvent.emit(event)
}