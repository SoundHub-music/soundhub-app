package com.soundhub.ui.login

sealed class AuthenticationEvent {
    data class OnLogin(val email: String, val password: String): AuthenticationEvent()
    data class OnRegister(val email: String, val password: String): AuthenticationEvent()
}