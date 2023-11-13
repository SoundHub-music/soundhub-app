package com.soundhub.ui.authentication

import com.soundhub.data.model.User

sealed class AuthenticationEvent {
    data class OnLogin(val email: String, val password: String): AuthenticationEvent()
    data class OnRegister(val user: User): AuthenticationEvent()
}