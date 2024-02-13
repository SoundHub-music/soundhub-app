package com.soundhub.ui.authentication

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.soundhub.data.datastore.UserPreferences
import com.soundhub.data.datastore.UserStore
import com.soundhub.data.model.User
import com.soundhub.data.repository.AuthRepository
import com.soundhub.ui.authentication.state.AuthFormState
import com.soundhub.UiStateDispatcher
import com.soundhub.data.model.Gender
import com.soundhub.ui.authentication.state.RegistrationState
import com.soundhub.utils.Constants
import com.soundhub.utils.Route
import com.soundhub.utils.UiEvent
import com.soundhub.utils.Validator
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.mindrot.jbcrypt.BCrypt
import java.lang.IllegalArgumentException
import java.time.LocalDate
import javax.inject.Inject

// TODO: separate the registration logic
@HiltViewModel
class AuthenticationViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val uiStateDispatcher: UiStateDispatcher,
    private val userStore: UserStore
) : ViewModel() {
    val userCreds: Flow<UserPreferences> = userStore.getCreds()
    var userInstance = MutableStateFlow<User?>(null)
        private set
    var authFormState = MutableStateFlow(AuthFormState())
        private set
    var registerState: MutableStateFlow<RegistrationState> = MutableStateFlow(RegistrationState())
        private set

    init {
        viewModelScope.launch {
            userInstance.update { userStore.getUser().firstOrNull() }
        }
    }

    override fun onCleared() {
        super.onCleared()
        Log.d("auth_viewmodel", "vm cleared")
    }

    fun resetAuthFormState() = viewModelScope.launch {
        authFormState.update { AuthFormState() }
    }

    fun resetRepeatedPassword() = viewModelScope.launch {
        authFormState.update {
            authFormState.value.copy(repeatedPassword = null)
        }
    }

    fun setEmail(value: String) = viewModelScope.launch {
        val isEmailValid = Validator.validateEmail(value)
        authFormState.update {
            it.copy(email = value, isEmailValid = isEmailValid)
        }
    }

    fun setPassword(value: String) = viewModelScope.launch {
        val isPasswordValid: Boolean = Validator.validatePassword(value)
        authFormState.update {
            it.copy(password = value, isPasswordValid = isPasswordValid)
        }
    }


    fun setRepeatedPassword(value: String) = viewModelScope.launch {
        val arePasswordsEqual: Boolean = Validator
            .arePasswordsEqual(authFormState.value.password, value)

        authFormState.update {
            it.copy(repeatedPassword = value, arePasswordsEqual = arePasswordsEqual)
        }
    }


    fun setAuthFormType(value: Boolean) = viewModelScope.launch {
        authFormState.update { it.copy(isRegisterForm = value) }
    }


    fun setFirstName(value: String) = registerState.update {
        it.copy(firstName = value, isFirstNameValid = value.isNotEmpty())
    }


    fun setLastName(value: String) = registerState.update {
        it.copy(lastName = value, isLastNameValid = value.isNotEmpty())
    }


    fun setBirthday(value: LocalDate?) = registerState.update {
        it.copy(birthday = value, isBirthdayValid = value != null)
    }


    fun setGender(value: String) = registerState.update {
        try {
            it.copy(gender = Gender.valueOf(value))
        } catch (exception: IllegalArgumentException) {
            it.copy(gender = Gender.Unknown)
        }
    }

    fun setCountry(value: String) = registerState.update { it.copy(country = value) }

    fun setCity(value: String) = registerState.update { it.copy(city = value) }
    fun setDescription(value: String) = registerState.update { it.copy(description = value) }

    fun logout() = viewModelScope.launch {
        Log.d(Constants.LOG_USER_CREDS_TAG, "AuthenticationViewModel[logout]: $userCreds")
        userStore.clear()
        uiStateDispatcher.sendUiEvent(UiEvent.Navigate(Route.Authentication))
    }

    fun authAction() {
        if (authFormState.value.isRegisterForm) {
            val salt = BCrypt.gensalt()
            val hashedPassword = BCrypt.hashpw(authFormState.value.password, salt)
            registerState.update {
                it.copy(
                    email = authFormState.value.email,
                    password = BCrypt.hashpw(hashedPassword, salt)
                )
            }
            onEvent(AuthEvent.OnChooseGenres)
        } else onEvent(AuthEvent.OnLogin(authFormState.value.email, authFormState.value.password))
    }

    fun onPostRegisterNextButtonClick(currentRoute: Route) {
        Log.d(
            Constants.LOG_REGISTER_STATE,
            "AuthenticationViewModel[onPostRegisterNextButtonClick]: ${registerState.value}"
        )
        when (currentRoute) {
            is Route.Authentication.ChooseGenres -> onEvent(AuthEvent.OnChooseArtists)
            is Route.Authentication.ChooseArtists -> onEvent(AuthEvent.OnFillUserData)
            is Route.Authentication.FillUserData ->
                if (Validator.validateRegistrationState(registerState.value))
                    onEvent(AuthEvent.OnRegister(User(registerState.value)))

            else -> Unit
        }
    }

    private fun onEvent(event: AuthEvent) {
        when (event) {
            is AuthEvent.OnLogin -> viewModelScope.launch {
                val salt = BCrypt.gensalt()
                val hashedPassword = BCrypt.hashpw(event.password, salt)
                val user: User? = authRepository.login(event.email, hashedPassword)
                if (user != null) {
                    userStore.saveUser(user)
                    uiStateDispatcher.sendUiEvent(UiEvent.Navigate(Route.Postline))
                    uiStateDispatcher.sendUiEvent(
                        UiEvent.ShowToast(
                            message = "You successfully logged in!\n" +
                                    "Your data: {email: ${event.email}, password: ${event.password}}"
                        )
                    )
                } else uiStateDispatcher.sendUiEvent(UiEvent.ShowToast("Неверный логин или пароль"))
            }


            is AuthEvent.OnRegister -> viewModelScope.launch {
                uiStateDispatcher.sendUiEvent(
                    UiEvent.ShowToast(
                        message = "You successfully signed up!\nYour data email: ${event.user.email}}",
                    )
                )
                authRepository.register(event.user)
                userStore.saveUser(event.user)
                uiStateDispatcher.sendUiEvent(UiEvent.Navigate(Route.Postline))
            }

            is AuthEvent.OnChooseGenres ->
                uiStateDispatcher.sendUiEvent(UiEvent.Navigate(Route.Authentication.ChooseGenres))

            is AuthEvent.OnChooseArtists ->
                uiStateDispatcher.sendUiEvent(UiEvent.Navigate(Route.Authentication.ChooseArtists))

            is AuthEvent.OnFillUserData ->
                uiStateDispatcher.sendUiEvent(UiEvent.Navigate(Route.Authentication.FillUserData))
        }
    }
}