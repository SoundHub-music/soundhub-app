package com.soundhub.ui.authentication.state

data class AuthValidationState(
    val isRegisterForm: Boolean = false,
    val email: String = "",
    val password: String = "",
    val repeatedPassword: String? = null,

    val isAuthSuccessful: Boolean = false,
    val isLoading: Boolean = false,

    val isEmailValid: Boolean = true,
    val isPasswordValid: Boolean = true,

    val isFormValid: Boolean = isEmailValid && isPasswordValid,
    val arePasswordsEqual: Boolean = true
)