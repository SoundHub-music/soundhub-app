package com.soundhub

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.soundhub.data.UserStore
import com.soundhub.utils.Routes
import com.soundhub.utils.UiEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UiEventDispatcher @Inject constructor(
    private val userDataStore: UserStore
): ViewModel() {
    private var _uiEvent = Channel<UiEvent>()
    val uiEvent: Flow<UiEvent> = _uiEvent.receiveAsFlow()

    init {
        viewModelScope.launch {
            userDataStore.getCreds().collect { creds ->
                // in the future we will check access token
                if (creds.id != null)
                    sendUiEvent(UiEvent.Navigate(Routes.Postline))
                else sendUiEvent(UiEvent.Navigate(Routes.Authentication))
            }

        }
    }

    fun sendUiEvent(event: UiEvent) {
        viewModelScope.launch {
            _uiEvent.send(event)
        }
    }
}