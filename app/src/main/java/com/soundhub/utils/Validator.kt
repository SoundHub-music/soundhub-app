package com.soundhub.utils

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

        fun validateAuthForm(email: String, password: String, repeatPassword: String?): Boolean {
            return validateEmail(email) &&
                    validatePassword(password) &&
                    arePasswordsEqual(password, repeatPassword ?: password) &&
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