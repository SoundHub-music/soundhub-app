package com.soundhub.ui.authentication.states

data class AuthFormState(
    val isRegisterForm: Boolean = false,
    val email: String = "",
    val password: String = "",
    val repeatedPassword: String? = null,

    val isLoading: Boolean = false,

    val isEmailValid: Boolean = true,
    val isPasswordValid: Boolean = true,

    val isFormValid: Boolean = isEmailValid && isPasswordValid,
    val arePasswordsEqual: Boolean = true
)