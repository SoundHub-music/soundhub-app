package com.soundhub.ui.viewmodels

import androidx.lifecycle.ViewModel
import com.soundhub.data.model.Message
import com.soundhub.data.model.User
import com.soundhub.data.websocket.WebSocketClient
import com.soundhub.ui.events.UiEvent
import com.soundhub.ui.states.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class UiStateDispatcher @Inject constructor() : ViewModel() {
    val uiEvent = Channel<UiEvent>()

    private val _uiState = MutableStateFlow(UiState())
    val uiState = _uiState.asStateFlow()

    fun clearState() = _uiState.update { UiState() }

    fun setWebSocketClient(webSocketClient: WebSocketClient) = _uiState.update {
        it.copy(webSocketClient = webSocketClient)
    }

    fun setCheckMessagesMode(check: Boolean) = _uiState.update {
        it.copy(isCheckMessagesMode = check)
    }

    fun unsetCheckMessagesMode() = _uiState.update {
        it.copy(isCheckMessagesMode = false, checkedMessages = emptyList())
    }

    fun uncheckMessage(message: Message) {
        if (_uiState.value.isCheckMessagesMode) {
            _uiState.update {
                val messages: List<Message> = it.checkedMessages.filter { msg -> msg != message }
                val isCheckMessagesMode = messages.isNotEmpty()
                it.copy(checkedMessages = messages, isCheckMessagesMode = isCheckMessagesMode)
            }
        }
    }

    fun addCheckedMessage(message: Message) = _uiState.update {
        it.copy(checkedMessages = it.checkedMessages + message)
    }

    fun setCurrentRoute(route: String?) = _uiState.update {
        it.copy(currentRoute = route)
    }

    fun setAuthorizedUser(user: User?) = _uiState.update {
        it.copy(authorizedUser = user)
    }

    fun toggleSearchBarActive() = _uiState.update {
        it.copy(isSearchBarActive = !it.isSearchBarActive, searchBarText = "")
    }

    fun setSearchBarActive(value: Boolean) = _uiState.update {
        it.copy(isSearchBarActive = value, searchBarText = "")
    }

    fun updateSearchBarText(value: String) = _uiState.update {
        it.copy(searchBarText = value)
    }

    fun getSearchBarText(): String = _uiState.value.searchBarText

    fun setGalleryUrls(value: List<String>) = _uiState.update {
        it.copy(galleryImageUrls = value)
    }

    suspend fun sendUiEvent(event: UiEvent) = uiEvent.send(event)
}