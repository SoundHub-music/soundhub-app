package com.soundhub.data.usecases

enum class AuthFieldValidationType {
    EmptyField,
    InvalidEmail,
    PasswordTooShort,
    PasswordsAreNotEqual,
    Valid
}