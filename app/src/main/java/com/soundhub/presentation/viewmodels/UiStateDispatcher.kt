package com.soundhub.presentation.viewmodels

import android.util.Log
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.soundhub.data.datastore.UserSettingsStore
import com.soundhub.data.datastore.model.UserSettings
import com.soundhub.domain.events.UiEvent
import com.soundhub.domain.model.Message
import com.soundhub.domain.model.User
import com.soundhub.domain.states.UiState
import com.soundhub.utils.constants.Constants
import com.soundhub.utils.enums.AppTheme
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class UiStateDispatcher @Inject constructor(
	private val userSettingsStore: UserSettingsStore
) : ViewModel() {
	private val userSettings: Flow<UserSettings> = userSettingsStore.getCreds()

	private val _uiEvent = Channel<UiEvent>()
	val uiEvent: Flow<UiEvent> = _uiEvent.receiveAsFlow()

	private val _uiState = MutableStateFlow(UiState())
	val uiState: Flow<UiState> = _uiState.asStateFlow()

	private val _receivedMessages = MutableSharedFlow<Message>(
		replay = 1,
		extraBufferCapacity = 100
	)

	val receivedMessages: Flow<Message> = _receivedMessages.asSharedFlow()

	private val _readMessages = MutableSharedFlow<Message>(
		replay = 1,
		extraBufferCapacity = 100
	)

	val readMessages: Flow<Message> = _readMessages.asSharedFlow()

	private val _deletedMessages = MutableSharedFlow<UUID>(
		replay = 1,
		extraBufferCapacity = 100
	)

	val deletedMessages: Flow<UUID> = _deletedMessages.asSharedFlow()

	override fun onCleared() {
		super.onCleared()
		Log.d("UiStateDispatcher", "onCleared: viewmodel was cleared")
	}

	fun clearState() = _uiState.update { UiState() }

	suspend fun sendReceivedMessage(message: Message) {
		if (!message.isRead)
			_receivedMessages.emit(message)
	}

	@OptIn(FlowPreview::class)
	fun onSearchValueDebounceChange(callback: suspend (String) -> Unit) = viewModelScope.launch {
		_uiState.map { it.searchBarText }.debounce(Constants.SEARCH_DEBOUNCE_VALUE)
			.distinctUntilChanged()
			.collect { value -> callback(value) }
	}

	suspend fun sendDeletedMessage(messageId: UUID) = _deletedMessages.emit(messageId)

	suspend fun sendReadMessage(message: Message) = _readMessages.emit(message)

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

	fun setSearchBarActive(value: Boolean) {
		if (_uiState.value.isSearchBarActive != value) {
			_uiState.update {
				it.copy(isSearchBarActive = value, searchBarText = "")
			}
		}
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

	fun setTheme(theme: AppTheme) = viewModelScope.launch {
		var settings = userSettings.firstOrNull()

		if (settings?.appTheme != theme) {
			settings = settings?.copy(appTheme = theme)
			userSettingsStore.updateCreds(settings)
		}
	}

	@Composable
	fun isDarkTheme(): Boolean {
		val settings by userSettings.collectAsState(initial = UserSettings())
		return when (settings.appTheme) {
			AppTheme.DARK -> true
			AppTheme.LIGHT -> false
			AppTheme.AUTO -> isSystemInDarkTheme()
		}
	}
}