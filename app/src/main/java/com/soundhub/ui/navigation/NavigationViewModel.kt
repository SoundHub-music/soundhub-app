package com.soundhub.ui.navigation

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.soundhub.utils.NamedScreens
import com.soundhub.utils.Routes
import com.soundhub.utils.UiEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NavigationViewModel @Inject constructor(): ViewModel() {
    var state = MutableStateFlow<NavigationState>(NavigationState())
        private set

    val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    fun updateState(newState: NavigationState) {
        viewModelScope.launch {
            state.emit(newState)
            _uiEvent.send(UiEvent.Navigate(newState.currentRoute))
        }
    }
}