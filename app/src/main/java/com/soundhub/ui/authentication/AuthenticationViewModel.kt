package com.soundhub.ui.authentication

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.soundhub.data.repository.UserRepository
import com.soundhub.utils.UiEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthenticationViewModel @Inject constructor(
    private val repository: UserRepository
): ViewModel() {

    private val _isLoggedIn = mutableStateOf(false)
    private val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    init {
        Log.d("ViewModel", "ViewModel is created")
    }

    override fun onCleared() {
        super.onCleared()
        Log.d("ViewModel", "Login ViewModel is cleared")
    }

    fun onEvent(event: AuthenticationEvent) {
        when (event) {
            is AuthenticationEvent.OnLogin -> {
                viewModelScope.launch {
                    sendUiEvent(UiEvent.ShowToast(
                        message = "You successfully logged in!\n" +
                                "Your data: {email: ${event.email}, password: ${event.password}}}"
                    ))
                    repository.login(event.email, event.password)
                }
            }
            is AuthenticationEvent.OnRegister -> {
                viewModelScope.launch {
                    sendUiEvent(UiEvent.ShowToast(
                        message = "You successfully signed up!\nYour data ${event.user.email}",
                        action = "Ok"
                    ))
                    repository.registerUser(event.user)
                }
            }
        }
    }

    private fun sendUiEvent(event: UiEvent) {
        viewModelScope.launch {
            _uiEvent.send(event)
        }
    }
}