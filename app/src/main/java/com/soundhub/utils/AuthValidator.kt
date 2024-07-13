package com.soundhub.utils

import com.soundhub.ui.authentication.AuthFormState
import com.soundhub.data.states.RegistrationState
import com.soundhub.utils.constants.Constants

sealed class AuthValidator {
    companion object {
        fun validateEmail(text: String): Boolean {
            val mask: String = Constants.EMAIL_REGEX
            if (text.isEmpty()) return true
            return text.matches(mask.toRegex())
        }

        fun arePasswordsEqual(password: String, repeatedPassword: String): Boolean {
            if (password.isEmpty() && repeatedPassword.isEmpty()) return true
            return password == repeatedPassword
        }

        fun validatePassword(password: String): Boolean {
            if (password.isEmpty()) return true
            return password.length >= Constants.PASSWORD_MIN_LENGTH
        }

        fun validateRegistrationState(registerState: RegistrationState): Boolean {
            return registerState.isFirstNameValid &&
                    registerState.isLastNameValid &&
                    registerState.isBirthdayValid
        }

        fun validateAuthForm(authFormState: AuthFormState): Boolean {
            val isLoginFormValid = authFormState.isEmailValid
                    && authFormState.isPasswordValid
                    && authFormState.email.isNotEmpty()
                    && authFormState.password.isNotEmpty()

            if (authFormState.isRegisterForm)
                return isLoginFormValid
                        && authFormState.arePasswordsEqual
                        && authFormState.repeatedPassword.isNotEmpty()

            return isLoginFormValid
        }
    }
}