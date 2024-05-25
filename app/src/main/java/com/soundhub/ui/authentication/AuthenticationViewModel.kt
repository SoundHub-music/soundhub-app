package com.soundhub.ui.authentication

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.soundhub.R
import com.soundhub.data.datastore.UserPreferences
import com.soundhub.data.datastore.UserCredsStore
import com.soundhub.data.model.User
import com.soundhub.ui.viewmodels.UiStateDispatcher
import com.soundhub.Route
import com.soundhub.ui.events.UiEvent
import com.soundhub.data.api.requests.RefreshTokenRequestBody
import com.soundhub.data.api.requests.SignInRequestBody
import com.soundhub.data.dao.UserDao
import com.soundhub.data.repository.AuthRepository
import com.soundhub.data.repository.UserRepository
import com.soundhub.utils.UiText
import com.soundhub.utils.Validator
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthenticationViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val uiStateDispatcher: UiStateDispatcher,
    private val userRepository: UserRepository,
    private val userCredsStore: UserCredsStore,
    private val userDao: UserDao
) : ViewModel() {
    private val authAttemptCount: MutableStateFlow<Int> = MutableStateFlow(0)
    private val maxRefreshTokenAttemptCount: Int = 2

    private val _authFormState = MutableStateFlow(AuthFormState())
    val authFormState = _authFormState.asStateFlow()
    val userCreds: Flow<UserPreferences> = userCredsStore.getCreds()

    init { viewModelScope.launch { initializeUser() } }

    override fun onCleared() {
        super.onCleared()
        Log.d("AuthenticationViewModel", "viewmodel was cleared")
    }

    private suspend fun initializeUser() {
        getCurrentUser().collect { currentUser ->
            currentUser?.let {
                val creds: UserPreferences? = userCreds.firstOrNull()
                var user = it
                if (!user.isOnline)
                    user = userRepository.toggleUserOnline(creds?.accessToken)
                        .onFailure { error -> Log.e("AuthenticationViewModel", "initializeUser[online status]: $error") }
                        .getOrNull() ?: it

                userDao.saveUser(user)
                uiStateDispatcher.setAuthorizedUser(user)
            }
        }
    }

    fun logout() = viewModelScope.launch(Dispatchers.IO) {
        Log.d("AuthenticationViewModel", "logout: $userCreds")
        authRepository
            .logout(userCreds.firstOrNull()?.accessToken)
            .onFailure { error ->
                val errorEvent: UiEvent = UiEvent.Error(
                    response = error.errorBody,
                    throwable = error.throwable
                )

                uiStateDispatcher.sendUiEvent(errorEvent)
            }
            .finally { deleteUserData() }
    }

    private suspend fun deleteUserData() {
        try {
            val currentUser: User? = userDao.getCurrentUser()

            currentUser?.let { user ->
                userDao.deleteUser(user)
                uiStateDispatcher.setAuthorizedUser(null)
                userCredsStore.clear()
            }
        }
        catch (e: Exception) {
            userDao.truncateUser()
        }
    }

    private suspend fun getCurrentUser(): Flow<User?> = flow {
        var authorizedUser: User? = userDao.getCurrentUser()

        if (authorizedUser == null) {
            val creds: UserPreferences? = userCreds.firstOrNull()
            userRepository.getCurrentUser(creds?.accessToken)
                .onSuccess { authorizedUser = it.body }
                .onFailure { tryRefreshToken() }
        }

        emit(authorizedUser)
    }

    suspend fun tryRefreshToken() {
        val creds: UserPreferences? = userCreds.firstOrNull()
        val requestBody = RefreshTokenRequestBody(creds?.refreshToken)

        authRepository.refreshToken(requestBody)
        .onSuccess { response ->
                userCredsStore.updateCreds(response.body)
                initializeUser()
                authAttemptCount.update { 0 }
        }.onFailure { error ->
            authAttemptCount.update { it + 1 }
            if (authAttemptCount.value <= maxRefreshTokenAttemptCount)
                tryRefreshToken()
            else {
                deleteUserData()

                val errorEvent: UiEvent = UiEvent.Error(error.errorBody, error.throwable)
                with(uiStateDispatcher) {
                    sendUiEvent(errorEvent)
                    sendUiEvent(UiEvent.Navigate(Route.Authentication))
                }
            }
        }
    }

    fun signIn() = viewModelScope.launch(Dispatchers.IO) {
        val toastErrorMessage: UiText.StringResource = UiText.StringResource(R.string.toast_authorization_error)
        _authFormState.update { it.copy(isLoading = true) }

        val ( email: String, password: String ) = authFormState.value
        val signInRequestBody = SignInRequestBody(email, password)

        authRepository.signIn(signInRequestBody)
            .onSuccess { response ->
                userCredsStore.updateCreds(response.body)

                getCurrentUser().firstOrNull()?.let { user ->
                    userDao.saveUser(user)
                    with(uiStateDispatcher) {
                        setAuthorizedUser(user)
                        sendUiEvent(UiEvent.Navigate(Route.PostLine))
                    }
                } ?: uiStateDispatcher.sendUiEvent(UiEvent.ShowToast(toastErrorMessage))
            }
            .onFailure { error ->
                val errorEvent: UiEvent = UiEvent.Error(error.errorBody, error.throwable)
                uiStateDispatcher.sendUiEvent(errorEvent)
            }
            .finally {
                _authFormState.update { it.copy(isLoading = false) }
            }
    }

    fun toggleUserOnline() = viewModelScope.launch {
        val creds: UserPreferences? = userCreds.firstOrNull()
        userRepository.toggleUserOnline(creds?.accessToken)
            .onSuccess { uiStateDispatcher.setAuthorizedUser(it.body) }
            .onFailure { error ->
                val errorEvent: UiEvent = UiEvent.Error(error.errorBody, error.throwable)
                uiStateDispatcher.sendUiEvent(errorEvent)
            }
    }

    fun resetAuthFormState() = _authFormState.update { AuthFormState() }

    fun resetRepeatedPassword() = _authFormState.update {
        it.copy(repeatedPassword = null)
    }

    fun setEmail(value: String) = _authFormState.update {
        val isEmailValid = Validator.validateEmail(value)
        it.copy(email = value, isEmailValid = isEmailValid)
    }

    fun setPassword(value: String) = _authFormState.update {
        val isPasswordValid: Boolean = Validator.validatePassword(value)
        it.copy(password = value, isPasswordValid = isPasswordValid)
    }

    fun setRepeatedPassword(value: String) = _authFormState.update {
        val arePasswordsEqual: Boolean = Validator
            .arePasswordsEqual(authFormState.value.password, value)
        it.copy(repeatedPassword = value, arePasswordsEqual = arePasswordsEqual)
    }

    fun setAuthFormType(value: Boolean) = _authFormState.update {
        it.copy(isRegisterForm = value)
    }
}