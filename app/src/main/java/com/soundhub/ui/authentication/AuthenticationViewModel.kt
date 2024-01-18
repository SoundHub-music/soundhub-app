package com.soundhub.ui.authentication

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.soundhub.data.UserPreferences
import com.soundhub.data.UserStore
import com.soundhub.data.model.User
import com.soundhub.data.repository.AuthRepository
import com.soundhub.ui.authentication.state.AuthValidationState
import com.soundhub.UiEventDispatcher
import com.soundhub.utils.Constants
import com.soundhub.utils.Routes
import com.soundhub.utils.UiEvent
import com.soundhub.utils.Validator
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthenticationViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val uiEventDispatcher: UiEventDispatcher,
    private val userStore: UserStore,
): ViewModel() {
    val userCreds: Flow<UserPreferences> = userStore.getCreds()
    var authValidationState by mutableStateOf(AuthValidationState())
        private set


    init {
        viewModelScope.launch {
            if (authValidationState.email.isEmpty() && authValidationState.password.isEmpty())
                authValidationState = authValidationState.copy(isEmailValid = true, isPasswordValid = true)
        }
    }

    fun resetState() {
        viewModelScope.launch {
            authValidationState = AuthValidationState()
        }
    }

    fun validateForm(): Boolean {
        val isLoginFormValid =  authValidationState.isEmailValid
                && authValidationState.isPasswordValid
                && authValidationState.email.isNotEmpty()
                && authValidationState.password.isNotEmpty()

        if (authValidationState.isRegisterForm)
            return isLoginFormValid
                    && authValidationState.arePasswordsEqual
                    && authValidationState.repeatedPassword?.isNotEmpty() ?: false
        return isLoginFormValid

    }

    fun resetRepeatedPassword() {
        viewModelScope.launch {
            authValidationState = authValidationState.copy(repeatedPassword = null)
        }
    }

    fun onEmailTextFieldChange(value: String) {
        viewModelScope.launch {
            val isEmailValid = Validator.validateEmail(value)
            authValidationState = authValidationState.copy(
                email = value,
                isEmailValid = isEmailValid,
            )
        }
    }

    fun onPasswordTextFieldChange(value: String) {
        viewModelScope.launch {
            val isPasswordValid: Boolean = Validator.validatePassword(value)
            authValidationState = authValidationState.copy(
                password = value,
                isPasswordValid = isPasswordValid,
            )
        }
    }

    fun onRepeatedPasswordTextFieldChange(value: String) {
        viewModelScope.launch {
            val arePasswordsEqual: Boolean = Validator
                .arePasswordsEqual(authValidationState.password, value)

            authValidationState = authValidationState.copy(
                repeatedPassword = value,
                arePasswordsEqual = arePasswordsEqual
            )
        }
    }

    fun onAuthTypeSwitchChange(value: Boolean) {
        viewModelScope.launch {
            authValidationState = authValidationState.copy(
                isRegisterForm = value
            )
        }
    }

    fun onFormButtonClick() {
        if (authValidationState.isRegisterForm)
            onEvent(AuthEvent.OnRegister(
                User(email = authValidationState.email, password = authValidationState.password)
            ))
        else
            onEvent(AuthEvent.OnLogin(authValidationState.email, authValidationState.password))
    }

    fun onLogoutButtonClick() {
        onEvent(AuthEvent.OnLogout)
    }

    private fun onEvent(event: AuthEvent) {
        when (event) {
            is AuthEvent.OnLogin -> {
                viewModelScope.launch {

                    val user: User? = authRepository.login(event.email, event.password)
                    if (user != null) {
                        userStore.saveUser(user)

                        uiEventDispatcher.sendUiEvent(UiEvent.Navigate(Routes.Postline))
                        uiEventDispatcher.sendUiEvent(UiEvent.ShowToast(
                            message = "You successfully logged in!\n" +
                                    "Your data: {email: ${event.email}}"
                        ))
                    }
                    else uiEventDispatcher.sendUiEvent(UiEvent.ShowToast("Неправильный логин или пароль"))
                }
            }

            is AuthEvent.OnRegister -> {
                viewModelScope.launch {
                    uiEventDispatcher.sendUiEvent(UiEvent.ShowToast(
                        message = "You successfully signed up!\nYour data ${event.user.email}",
                    ))
                    authRepository.register(event.user)
                    userStore.saveUser(event.user)

                    uiEventDispatcher.sendUiEvent(UiEvent.Navigate(Routes.Postline))
                }
            }

            is AuthEvent.OnLogout -> {
                viewModelScope.launch {
                    userStore.clear()
                    Log.d(Constants.LOG_USER_CREDS_TAG, "AuthenticationViewModel[onEvent]: $userCreds")
                    uiEventDispatcher.sendUiEvent(UiEvent.Navigate(Routes.Authentication))
                }
            }
        }
    }
}