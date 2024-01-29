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
class UiEventDispatcher @Inject constructor(
    private val userDataStore: UserStore
): ViewModel() {
    private var _uiEvent = Channel<UiEvent>()
    val uiEvent: Flow<UiEvent> = _uiEvent.receiveAsFlow()
    var currentRoute: MutableStateFlow<Route> = MutableStateFlow(Route.Authentication)
        private set

    init {
        viewModelScope.launch {
            userDataStore.getCreds().collect { creds ->
                // in the future we will check access token
                // TODO: change id to session token
                if (creds.id != null) {
                    currentRoute.update { Route.Postline }
                    sendUiEvent(UiEvent.Navigate(Route.Postline))
                }
                else {
                    currentRoute.update { Route.Authentication }
                    sendUiEvent(UiEvent.Navigate(Route.Authentication))
                }
            }

        }
    }

    fun sendUiEvent(event: UiEvent) {
        viewModelScope.launch {
            if (event is UiEvent.Navigate)
                currentRoute.update { event.route }
            _uiEvent.send(event)
        }
    }
}