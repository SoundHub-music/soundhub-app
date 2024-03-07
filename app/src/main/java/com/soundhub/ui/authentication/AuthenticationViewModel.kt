package com.soundhub.ui.authentication

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.soundhub.data.datastore.UserPreferences
import com.soundhub.data.datastore.UserStore
import com.soundhub.data.model.User
import com.soundhub.ui.authentication.state.AuthFormState
import com.soundhub.UiStateDispatcher
import com.soundhub.data.model.Gender
import com.soundhub.ui.authentication.state.RegistrationState
import com.soundhub.utils.Constants
import com.soundhub.Route
import com.soundhub.UiEvent
import com.soundhub.data.model.ApiResult
import com.soundhub.data.api.LogoutRequestBody
import com.soundhub.data.api.LogoutResponse
import com.soundhub.data.api.RefreshTokenRequestBody
import com.soundhub.data.api.SignInRequestBody
import com.soundhub.data.api.RegisterRequestBody
import com.soundhub.data.repository.AuthRepository
import com.soundhub.data.repository.UserRepository
import com.soundhub.utils.Validator
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.lang.IllegalArgumentException
import java.time.LocalDate
import javax.inject.Inject

// TODO: separate the registration logic
@HiltViewModel
class AuthenticationViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val uiStateDispatcher: UiStateDispatcher,
    private val userRepository: UserRepository,
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
            userCreds.collect { creds ->
                if (creds.accessToken != null) {
                    userInstance.value = getCurrentUser().firstOrNull()
                }
            }
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


    fun setGender(value: String) {
        try {
            registerState.update {
                it.copy(gender = Gender.valueOf(value))
            }
        }
        catch (e: IllegalArgumentException) {
            Log.e("set_gender_error", e.message ?: "error")
            registerState.update {
                it.copy(gender = Gender.UNKNOWN)
            }
        }
    }

    fun setCountry(value: String) = registerState.update { it.copy(country = value) }

    fun setCity(value: String) = registerState.update { it.copy(city = value) }
    fun setDescription(value: String) = registerState.update { it.copy(description = value) }

    fun setLanguages(languages: List<String>) = registerState.update {
        it.copy(languages = languages)
    }

    fun setLanguages(language: String) = registerState.update {
        it.copy(languages = it.languages + language)
    }

    fun logout() = viewModelScope.launch {
        Log.d(Constants.LOG_USER_CREDS_TAG, "AuthenticationViewModel[logout]: $userCreds")
        val tokens = userCreds.firstOrNull()
        val logoutResponse: ApiResult<LogoutResponse> = authRepository.logout(
            LogoutRequestBody(
                accessToken = tokens?.accessToken,
                refreshToken = tokens?.refreshToken
            )
        )
        when (logoutResponse) {
            is ApiResult.Success -> {
                userStore.clear()
                uiStateDispatcher.sendUiEvent(UiEvent.Navigate(Route.Authentication))
            }
            is ApiResult.Error -> {
                uiStateDispatcher.sendUiEvent(UiEvent.ShowToast("Ошибка!"))
                return@launch
            }
        }
    }

    fun authAction() {
        if (authFormState.value.isRegisterForm) {
            registerState.update {
                it.copy(
                    email = authFormState.value.email,
                    password = authFormState.value.password
                )
            }
            onEvent(AuthEvent.OnChooseGenres)
        } else onEvent(AuthEvent.OnSignIn(authFormState.value.email, authFormState.value.password))
    }

    fun onPostRegisterNextButtonClick(currentRoute: Route) {
        Log.d(
            Constants.LOG_REGISTER_STATE,
            "AuthenticationViewModel[onPostRegisterNextButtonClick]: ${registerState.value}"
        )
        when (currentRoute) {
            is Route.Authentication.ChooseGenres -> onEvent(AuthEvent.OnChooseArtists)
            is Route.Authentication.ChooseArtists -> onEvent(AuthEvent.OnFillUserData)
            is Route.Authentication.FillUserData -> {
                registerState.update {
                    it.copy(
                        isFirstNameValid = it.firstName?.isNotEmpty() ?: false,
                        isLastNameValid = it.lastName?.isNotEmpty() ?: false,
                        isBirthdayValid = it.birthday != null
                    )
                }

                if (Validator.validateRegistrationState(registerState.value))
                    onEvent(AuthEvent.OnRegister(User(registerState.value)))
            }
            else -> Unit
        }
    }

    private suspend fun getCurrentUser(): Flow<User?> = flow {
        val creds = userCreds.firstOrNull()
        val currentUserResponse: ApiResult<User?> = userRepository.getCurrentUser(creds?.accessToken)

        when (currentUserResponse) {
            is ApiResult.Success -> { emit(currentUserResponse.data) }
            is ApiResult.Error -> {
                if (currentUserResponse.code == 401) {
                    val newCreds: ApiResult<UserPreferences?> = authRepository.refreshToken(
                        RefreshTokenRequestBody(creds?.refreshToken)
                    )
                    userStore.updateCreds(newCreds.data)
                    getCurrentUser()
                }
            }
        }

    }

    private suspend fun onSignInEvent(event: AuthEvent.OnSignIn) {
        try {
            uiStateDispatcher.sendUiEvent(UiEvent.Loading(true))
            val signInResponse: ApiResult<UserPreferences?> = authRepository.signIn(
                SignInRequestBody(
                    email = event.email,
                    password = event.password
                )
            )

            Log.d("sign_in_response", signInResponse.toString())

            when (signInResponse) {
                is ApiResult.Success -> {
                    userStore.updateCreds(signInResponse.data)
                    val currentUser: User? = getCurrentUser().firstOrNull()


                    userInstance.update { currentUser }
                    uiStateDispatcher.sendUiEvent(UiEvent.Navigate(Route.Postline))
                }
                is ApiResult.Error -> {
                    uiStateDispatcher.sendUiEvent(UiEvent.ShowToast(signInResponse.message ?: ""))
                    return
                }
            }
        }
        catch (e: Exception) {
            uiStateDispatcher.sendUiEvent(UiEvent.ShowToast("Произошла критическая ошибка: ${e.message}"))
        }
        finally {
            uiStateDispatcher.sendUiEvent(UiEvent.Loading(false))
        }
    }

    private fun onEvent(event: AuthEvent) {
        when (event) {
            is AuthEvent.OnSignIn -> viewModelScope.launch(Dispatchers.IO) {
                onSignInEvent(event)
            }

            is AuthEvent.OnRegister -> viewModelScope.launch {
                uiStateDispatcher.sendUiEvent(
                    UiEvent.ShowToast(
                        message = "You successfully signed up!\nYour data email: ${event.user.email}}",
                    )
                )
                val requestBody = RegisterRequestBody(registerState.value)
                val registerResponse: ApiResult<UserPreferences?> = authRepository.signUp(requestBody)
                Log.d("register_response", registerResponse.data.toString())

                when (registerResponse) {
                    is ApiResult.Success -> {
                        userStore.updateCreds(registerResponse.data)
                        userInstance.update { event.user }
                        uiStateDispatcher.sendUiEvent(UiEvent.Navigate(Route.Postline))
                    }

                    is ApiResult.Error -> {
                        UiEvent.ShowToast("${registerResponse.code}: ${registerResponse.message}")
                    }
                }
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