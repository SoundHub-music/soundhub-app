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

@HiltViewModel
class UiEventDispatcher @Inject constructor(private val userDataStore: UserStore) : ViewModel() {
    private var _uiEvent = Channel<UiEvent>()
    val uiEvent: Flow<UiEvent> = _uiEvent.receiveAsFlow()
    var isSearchBarActive = MutableStateFlow(false)
        private set
    var searchBarText = MutableStateFlow("")
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

    fun toggleSearchBarActive() = isSearchBarActive.update { !isSearchBarActive.value }
    fun setSearchBarActive(value: Boolean) { isSearchBarActive.value = value }
    fun updateSearchBarText(value: String) = searchBarText.update { value }

    fun sendUiEvent(event: UiEvent) = viewModelScope.launch { _uiEvent.send(event) }
}