package com.soundhub.ui.authentication

import com.soundhub.data.model.User

sealed class AuthEvent {
    data class OnLogin(val email: String, val password: String): AuthEvent()
    data class OnRegister(val user: User): AuthEvent()
    object OnChooseGenres: AuthEvent()
    object OnChooseArtists: AuthEvent()
    object OnFillUserData: AuthEvent()
}