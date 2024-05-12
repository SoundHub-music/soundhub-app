package com.soundhub.ui.authentication

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.soundhub.data.datastore.UserPreferences
import com.soundhub.data.datastore.UserCredsStore
import com.soundhub.data.model.User
import com.soundhub.ui.viewmodels.UiStateDispatcher
import com.soundhub.Route
import com.soundhub.ui.events.UiEvent
import com.soundhub.data.api.requests.RefreshTokenRequestBody
import com.soundhub.data.api.requests.SignInRequestBody
import com.soundhub.data.api.responses.HttpResult
import com.soundhub.data.dao.UserDao
import com.soundhub.data.database.AppDatabase
import com.soundhub.data.repository.AuthRepository
import com.soundhub.data.repository.UserRepository
import com.soundhub.ui.authentication.states.AuthFormState
import com.soundhub.utils.UiText
import com.soundhub.utils.Validator
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthenticationViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val uiStateDispatcher: UiStateDispatcher,
    private val userRepository: UserRepository,
    private val userCredsStore: UserCredsStore,
    appDb: AppDatabase
) : ViewModel() {
    private val authAttemptCount: MutableStateFlow<Int> = MutableStateFlow(0)
    private val userDao: UserDao = appDb.userDao()
    private val maxRefreshTokenAttemptCount: Int = 2

    val authFormState = MutableStateFlow(AuthFormState())
    val userCreds: Flow<UserPreferences> = userCredsStore.getCreds()

    init {
        viewModelScope.launch {
            initializeUser()
        }
    }

    override fun onCleared() {
        super.onCleared()
        Log.d("AuthenticationViewModel", "viewmodel was cleared")
    }

    private suspend fun initializeUser() {
        val authorizedUser = getCurrentUser()
        authorizedUser.collect { currentUser ->
            currentUser?.let {
                userDao.saveUser(currentUser)
                uiStateDispatcher.setAuthorizedUser(currentUser)
            }
        }
    }

    fun logout() = viewModelScope.launch(Dispatchers.IO) {
        Log.d("AuthenticationViewModel", "logout: $userCreds")
        authRepository
            .logout(userCreds.firstOrNull()?.accessToken)
            .onFailure {
                val toastText: UiText.DynamicString = UiText.DynamicString(it.errorBody.detail ?: "")
                uiStateDispatcher.sendUiEvent(UiEvent.ShowToast(toastText))
            }
            .finally { deleteUserData() }
    }

    private suspend fun deleteUserData() {
        val currentUser: User? = uiStateDispatcher.uiState
            .map { it.authorizedUser }
            .firstOrNull()

        currentUser?.let { user ->
            userDao.deleteUser(user)
            uiStateDispatcher.setAuthorizedUser(null)
        }
        userCredsStore.clear()
    }

    private suspend fun getCurrentUser(): Flow<User?> = flow {
        val creds: UserPreferences? = userCreds.firstOrNull()
        userRepository.getCurrentUser(creds?.accessToken)
            .onSuccess { emit(it.body) }
            .onFailure { error ->
                tryRefreshToken(
                    error = error,
                    userCreds = creds
                )
            }
    }

    private suspend fun tryRefreshToken(
        error: HttpResult.Error<User?>,
        userCreds: UserPreferences?
    ) {
        val requestBody = RefreshTokenRequestBody(userCreds?.refreshToken)
        authRepository.refreshToken(requestBody)
            .onSuccess { response ->
                userCredsStore.updateCreds(response.body)
                initializeUser()
                authAttemptCount.update { 0 }
        }.onFailure {
            authAttemptCount.update { it + 1 }
            if (authAttemptCount.value <= maxRefreshTokenAttemptCount)
                tryRefreshToken(error, userCreds)
            else {
                val toastText: UiText.DynamicString =  UiText.DynamicString(error.errorBody.detail ?: "")
                deleteUserData()

                with(uiStateDispatcher) {
                    sendUiEvent(UiEvent.ShowToast(toastText))
                    sendUiEvent(UiEvent.Navigate(Route.Authentication))
                }
            }
        }
    }

    fun signIn() = viewModelScope.launch(Dispatchers.IO) {
        authFormState.update { it.copy(isLoading = true) }
        authRepository.signIn(
            SignInRequestBody(
                email = authFormState.value.email,
                password = authFormState.value.password
            ))
            .onSuccess { response ->
                userCredsStore.updateCreds(response.body)
                val currentUser: User? = getCurrentUser().firstOrNull()

                with(uiStateDispatcher) {
                    setAuthorizedUser(currentUser)
                    sendUiEvent(UiEvent.Navigate(Route.Postline))
                }
            }
            .onFailure {
                val toastText = UiText.DynamicString(it.errorBody.detail ?: "")
                uiStateDispatcher.sendUiEvent(UiEvent.ShowToast(toastText))
            }
            .finally {
                authFormState.update { it.copy(isLoading = false) }
            }
    }

    fun resetAuthFormState() = authFormState.update { AuthFormState() }

    fun resetRepeatedPassword() = authFormState.update {
        it.copy(repeatedPassword = null)
    }

    fun setEmail(value: String) = authFormState.update {
        val isEmailValid = Validator.validateEmail(value)
        it.copy(email = value, isEmailValid = isEmailValid)
    }

    fun setPassword(value: String) = authFormState.update {
        val isPasswordValid: Boolean = Validator.validatePassword(value)
        it.copy(password = value, isPasswordValid = isPasswordValid)
    }

    fun setRepeatedPassword(value: String) = authFormState.update {
        val arePasswordsEqual: Boolean = Validator
            .arePasswordsEqual(authFormState.value.password, value)
        it.copy(repeatedPassword = value, arePasswordsEqual = arePasswordsEqual)
    }

    fun setAuthFormType(value: Boolean) = authFormState.update {
        it.copy(isRegisterForm = value)
    }
}