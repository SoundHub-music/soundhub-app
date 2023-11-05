package com.soundhub.ui.login

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import com.soundhub.data.login.AuthenticationState

class AuthenticationViewModel: ViewModel() {
    private var authData = MutableLiveData<AuthenticationState>()
    val value = authData.asFlow()

    init {
        Log.d("ViewModel", "ViewModel is created")
    }

    override fun onCleared() {
        super.onCleared()
        Log.e("ViewModel", "Login ViewModel is cleared")
    }

    fun onEvent(event: AuthenticationEvent) {
        when (event) {
            is AuthenticationEvent.OnLogin -> login(event.email, event.password)
            is AuthenticationEvent.OnRegister -> register(event.email, event.password)
        }
    }

    private fun login(email: String, password: String) {
        Log.i(
            "Authentication",
            "User succesfully logged in: email: $email, password: $password"
        )
    }

    private fun register(email: String, password: String) {
        Log.i(
            "Authentication",
            "User was succesfully registered: email: $email, password: $password"
        )
    }
}