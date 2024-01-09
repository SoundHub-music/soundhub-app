package com.soundhub.data.usecases

import com.soundhub.utils.Validator

class AuthFormValidationUseCase() {
    operator fun invoke(
        email: String,
        password: String,
        repeatedPassword: String? = null,
        isRegisterForm: Boolean
    ): AuthFieldValidationType {
        if (isRegisterForm)
            return checkRegister(email, password, repeatedPassword!!)

        if (!Validator.validateEmail(email))
            return AuthFieldValidationType.InvalidEmail

        if (email.isEmpty() || password.isEmpty())
            return AuthFieldValidationType.EmptyField

        if (!Validator.validatePassword(password))
            return AuthFieldValidationType.PasswordTooShort

        return AuthFieldValidationType.Valid
    }


    private fun checkRegister(email: String, password: String, repeatedPassword: String): AuthFieldValidationType {
        if (email.isEmpty() || password.isEmpty() || repeatedPassword.isEmpty())
            return AuthFieldValidationType.EmptyField

        if (Validator.arePasswordsEqual(password, repeatedPassword))
            return AuthFieldValidationType.PasswordsAreNotEqual

        return AuthFieldValidationType.Valid
    }
}