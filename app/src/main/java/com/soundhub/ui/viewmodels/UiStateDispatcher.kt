package com.soundhub.ui.viewmodels

import androidx.lifecycle.ViewModel
import com.soundhub.data.model.Message
import com.soundhub.data.model.User
import com.soundhub.ui.events.UiEvent
import com.soundhub.ui.states.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class UiStateDispatcher @Inject constructor() : ViewModel() {
    private val _uiEvent = Channel<UiEvent>()
    val uiEvent: Flow<UiEvent> = _uiEvent.receiveAsFlow()

    private val _uiState = MutableStateFlow(UiState())
    val uiState: Flow<UiState> = _uiState.asStateFlow()

    private val _receivedMessages = Channel<Message>()
    val receivedMessages: Flow<Message> = _receivedMessages.receiveAsFlow()

    private val _receivedMessagesCount = MutableStateFlow<UInt>(0u)
    val receivedMessagesCount: Flow<UInt> = _receivedMessagesCount.asStateFlow()

    private val _readMessages = Channel<Message>()
    val readMessages: Flow<Message> = _readMessages.receiveAsFlow()

    private val _deletedMessages = Channel<Message>()
    val deletedMessages: Flow<Message> = _deletedMessages.receiveAsFlow()

    fun clearState() = _uiState.update { UiState() }

    suspend fun sendReceivedMessage(message: Message) {
        if (!message.isRead)
            _receivedMessages.send(message)
        _receivedMessagesCount.update { it.inc() }
    }

    suspend fun sendDeletedMessage(message: Message) = _deletedMessages.send(message)

    suspend fun sendReadMessage(message: Message) {
        val authorizedUser: User? = uiState.map { it.authorizedUser }
            .firstOrNull()

        if (message.sender?.id != authorizedUser?.id && _receivedMessagesCount.value > 0u)
            _receivedMessagesCount.update { it.dec() }
        _readMessages.send(message)
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

    // search bar visibility
    fun toggleSearchBarActive() = _uiState.update {
        it.copy(isSearchBarActive = !it.isSearchBarActive, searchBarText = "")
    }

    fun setSearchBarActive(value: Boolean) = _uiState.update {
        it.copy(isSearchBarActive = value, searchBarText = "")
    }


    // search bar content
    fun updateSearchBarText(value: String) = _uiState.update {
        it.copy(searchBarText = value)
    }

    fun getSearchBarText(): String = _uiState.value.searchBarText



    fun setGalleryUrls(value: List<String>) = _uiState.update {
        it.copy(galleryImageUrls = value)
    }

    suspend fun sendUiEvent(event: UiEvent) = _uiEvent.send(event)
}