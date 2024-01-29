package com.soundhub.utils

import com.soundhub.ui.authentication.state.RegistrationState

sealed class Validator {
    companion object {
        fun validateEmail(text: String): Boolean {
            val mask: String = Constants.EMAIL_MASK
            if (text.isEmpty()) return true
            return text.matches(mask.toRegex())
        }

        fun arePasswordsEqual(password: String, repeatPassword: String): Boolean {
            return password == repeatPassword && (password.isNotEmpty() && repeatPassword.isNotEmpty())
        }

        fun validatePassword(password: String): Boolean {
            if (password.isEmpty()) return true
            return password.length >= Constants.PASSWORD_MIN_LENGTH
        }

        fun validateRegistrationState(registerState: RegistrationState): Boolean {
            registerState.isFirstNameValid = registerState.firstName.isNotEmpty()
            registerState.isLastNameValid = registerState.lastName.isNotEmpty()
            registerState.isBirthdayValid = registerState.birthday != null

            return registerState.isFirstNameValid &&
                    registerState.isLastNameValid &&
                    registerState.isBirthdayValid
        }
    }
}