package com.soundhub.data.login

data class AuthenticationState(
    val email: String = "",
    val password: String = "",
    val repeatedPassword: String = ""
)