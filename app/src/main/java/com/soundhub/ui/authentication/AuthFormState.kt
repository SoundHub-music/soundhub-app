package com.soundhub.ui.authentication

data class AuthFormState(
    val email: String = "",
    val password: String = "",
    val repeatedPassword: String = "",
    val isRegisterForm: Boolean = false,

    val isLoading: Boolean = false,

    val isEmailValid: Boolean = true,
    val isPasswordValid: Boolean = true,

    val isFormValid: Boolean = isEmailValid && isPasswordValid,
    val arePasswordsEqual: Boolean = true
)