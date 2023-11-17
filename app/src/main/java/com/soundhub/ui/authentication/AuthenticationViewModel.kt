package com.soundhub.ui.authentication

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.soundhub.data.repository.UserRepository
import com.soundhub.utils.Routes
import com.soundhub.utils.UiEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthenticationViewModel @Inject constructor(
    private val repository: UserRepository,
): ViewModel() {

    private var _isLoggedIn = mutableStateOf(false)
    val isLoggedIn: State<Boolean> = _isLoggedIn

    private val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    init {
        Log.d("ViewModel", "ViewModel was created")
    }

    override fun onCleared() {
        super.onCleared()
        Log.d("ViewModel", "Login ViewModel is cleared")
    }

    fun onEvent(event: AuthEvent) {
        when (event) {
            is AuthEvent.OnLogin -> {
                viewModelScope.launch {
                    sendUiEvent(UiEvent.ShowToast(
                        message = "You successfully logged in!\n" +
                                "Your data: {email: ${event.email}}"
                    ))
                    repository.login(event.email)
                    sendUiEvent(UiEvent.Navigate(Routes.POSTLINE))
                    _isLoggedIn.value = true
                }
            }
            is AuthEvent.OnRegister -> {
                viewModelScope.launch {
                    sendUiEvent(UiEvent.ShowToast(
                        message = "You successfully signed up!\nYour data ${event.user.email}",
                    ))
                    repository.registerUser(event.user)
                    sendUiEvent(UiEvent.Navigate(Routes.POSTLINE))
                    _isLoggedIn.value = true
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