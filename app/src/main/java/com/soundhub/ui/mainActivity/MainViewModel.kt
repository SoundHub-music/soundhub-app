package com.soundhub.ui.mainActivity

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.soundhub.utils.Routes
import com.soundhub.utils.UiEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(): ViewModel() {
    private var _currentRoute = MutableStateFlow<Routes>(Routes.AppStart)

    private var _uiEvent = Channel<UiEvent>()
    val uiEvent: Flow<UiEvent> = _uiEvent.receiveAsFlow()

    init {
        Log.d("route", _currentRoute.value.route)
    }

    fun onEvent(event: UiEvent) {
        viewModelScope.launch {
            Log.d("event", event.toString())
            when(event) {
                is UiEvent.Navigate -> {
                    _currentRoute.value = event.route
                    Log.d("route", _currentRoute.value.route)
                }
                else -> Unit
            }
        }
    }

    fun sendUiEvent(uiEvent: UiEvent) {
        viewModelScope.launch {
            _uiEvent.send(uiEvent)
        }
    }
}