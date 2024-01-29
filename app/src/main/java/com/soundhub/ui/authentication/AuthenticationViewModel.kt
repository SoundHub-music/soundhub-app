package com.soundhub.ui.authentication

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.soundhub.data.datastore.UserPreferences
import com.soundhub.data.datastore.UserStore
import com.soundhub.data.model.User
import com.soundhub.data.repository.AuthRepository
import com.soundhub.ui.authentication.state.AuthFormState
import com.soundhub.UiEventDispatcher
import com.soundhub.ui.authentication.state.RegistrationState
import com.soundhub.utils.Constants
import com.soundhub.utils.Route
import com.soundhub.utils.UiEvent
import com.soundhub.utils.Validator
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.mindrot.jbcrypt.BCrypt
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class AuthenticationViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val uiEventDispatcher: UiEventDispatcher,
    private val userStore: UserStore,
): ViewModel() {
    val userCreds: Flow<UserPreferences> = userStore.getCreds()

    private var _authFormState = MutableStateFlow(AuthFormState())
    val authFormState = _authFormState.asStateFlow()

    private var _registerState: MutableStateFlow<RegistrationState> = MutableStateFlow(RegistrationState())
    val registerState: StateFlow<RegistrationState> = _registerState.asStateFlow()

    init {
        viewModelScope.launch {
            if (_authFormState.value.email.isEmpty() && _authFormState.value.password.isEmpty())
                _authFormState.update {
                    _authFormState.value.copy(isEmailValid = true, isPasswordValid = true)
                }
        }
    }

    fun resetState() {
        viewModelScope.launch {
            _authFormState.update { AuthFormState() }
        }
    }

    fun validateForm(): Boolean {
        val isLoginFormValid = _authFormState.value.isEmailValid
                && _authFormState.value.isPasswordValid
                && _authFormState.value.email.isNotEmpty()
                && _authFormState.value.password.isNotEmpty()

        if (_authFormState.value.isRegisterForm)
            return isLoginFormValid
                    && _authFormState.value.arePasswordsEqual
                    && _authFormState.value.repeatedPassword?.isNotEmpty() ?: false

        return isLoginFormValid
    }

    fun resetRepeatedPassword() {
        viewModelScope.launch {
            _authFormState.update {
                _authFormState.value.copy(repeatedPassword = null)
            }
        }
    }

    fun onEmailTextFieldChange(value: String) {
        viewModelScope.launch {
            val isEmailValid = Validator.validateEmail(value)
            _authFormState.update {
                it.copy(email = value, isEmailValid = isEmailValid,)
            }
        }
    }

    fun onPasswordTextFieldChange(value: String) {
        viewModelScope.launch {
            val isPasswordValid: Boolean = Validator.validatePassword(value)
            _authFormState.update {
                it.copy(password = value, isPasswordValid = isPasswordValid,)
            }
        }
    }

    fun onRepeatedPasswordTextFieldChange(value: String) {
        viewModelScope.launch {
            val arePasswordsEqual: Boolean = Validator
                .arePasswordsEqual(_authFormState.value.password, value)

            _authFormState.update {
                it.copy(repeatedPassword = value, arePasswordsEqual = arePasswordsEqual)
            }
        }
    }

    fun onAuthTypeSwitchChange(value: Boolean) {
        viewModelScope.launch {
            _authFormState.update { it.copy(isRegisterForm = value) }
        }
    }

    fun onAuthFormButtonClick() {
        if (_authFormState.value.isRegisterForm) {
//            val salt = BCrypt.gensalt()
//            _registerState.update {
//                it.copy(
//                    email = _authFormState.value.email,
//                    password = BCrypt.hashpw(_authFormState.value.password, salt)
//                )
//            }
//            onEvent(AuthEvent.OnChooseGenres)
            onEvent(
                AuthEvent.OnRegister(
                    User(
                        email = _authFormState.value.email,
                        password = _authFormState.value.password
                    )
                )
            )
        }
        else
            onEvent(AuthEvent.OnLogin(
                _authFormState.value.email, _authFormState.value.password
            ))
    }

    fun onPostRegisterNextButtonClick(currentRoute: Route) {
        when (currentRoute) {
            is Route.Authentication.ChooseGenres -> onEvent(AuthEvent.OnChooseArtists)
            is Route.Authentication.ChooseArtists -> onEvent(AuthEvent.OnFillUserData)
            is Route.Authentication.FillUserData -> {
                _registerState.update {
                    it.copy(
                        email = _authFormState.value.email, password = _authFormState.value.password
                    )
                }

                if (Validator.validateRegistrationState(_registerState.value))
                    onEvent(AuthEvent.OnRegister(User(registerState.value)))
            }
            else -> Unit
        }
    }

    fun onFirstNameTextFieldChange(value: String) {
        val isFirstNameValid: Boolean = value.isNotEmpty()
        _registerState.update {
            it.copy(firstName = value, isFirstNameValid = isFirstNameValid)
        }
    }

    fun onLastNameTextFieldChange(value: String) {
        val isLastNameValid: Boolean = value.isNotEmpty()
        _registerState.update {
            it.copy(lastName = value, isLastNameValid = isLastNameValid)
        }
    }

    fun onBirthdayTextFieldChange(value: LocalDate?) {
        val isBirthdayValid = value != null
        _registerState.update {
            it.copy(birthday = value, isBirthdayValid = isBirthdayValid)
        }
    }

    fun onLogoutButtonClick() {
        onEvent(AuthEvent.OnLogout)
    }

    private fun onEvent(event: AuthEvent) {
        when (event) {
            is AuthEvent.OnLogin -> {
                viewModelScope.launch {
                    val salt = BCrypt.gensalt()
                    val hashedPassword = BCrypt.hashpw(event.password, salt)
                    val user: User? = authRepository.login(event.email, hashedPassword)
                    if (user != null) {
                        userStore.saveUser(user)

                        uiEventDispatcher.sendUiEvent(UiEvent.Navigate(Route.Postline))
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

                    uiEventDispatcher.sendUiEvent(UiEvent.Navigate(Route.Postline))
                }
            }

            is AuthEvent.OnLogout -> {
                viewModelScope.launch {
                    userStore.clear()
                    Log.d(Constants.LOG_USER_CREDS_TAG, "AuthenticationViewModel[onEvent]: $userCreds")
                    uiEventDispatcher.sendUiEvent(UiEvent.Navigate(Route.Authentication))
                }
            }
            is AuthEvent.OnChooseGenres -> {
                uiEventDispatcher.sendUiEvent(UiEvent.Navigate(Route.Authentication.ChooseGenres))
            }

            is AuthEvent.OnChooseArtists -> {
                uiEventDispatcher.sendUiEvent(UiEvent.Navigate(Route.Authentication.ChooseArtists))
            }

            is AuthEvent.OnFillUserData -> {
                uiEventDispatcher.sendUiEvent(UiEvent.Navigate(Route.Authentication.FillUserData))
            }
        }
    }
}