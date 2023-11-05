package com.soundhub.utils

sealed class Validator {
    companion object {
        fun validateEmail(text: String): Boolean {
            val mask = "^[A-Za-z](.*)(@)(.+)(\\.)(.+)"
            if (text.isEmpty()) return true
            return text.matches(mask.toRegex())
        }

        fun arePasswordsNotEquals(password: String, repeatPassword: String): Boolean {
            return password != repeatPassword && (password.isNotEmpty() && repeatPassword.isNotEmpty())
        }

        fun validatePassword(password: String): Boolean {
            if (password.isEmpty()) return true
            return password.length >= 6
        }

        fun validateAuthForm(email: String, password: String, repeatPassword: String?): Boolean {
            return validateEmail(email) &&
                    validatePassword(password) &&
                    !arePasswordsNotEquals(password, repeatPassword ?: password) &&
                    areFieldsNotEmpty(email, password, repeatPassword )
        }

        private fun areFieldsNotEmpty(vararg fields: String?): Boolean {
            for (field in fields) {
                if (field?.isEmpty() == true) return false
            }
            return true
        }
    }
}