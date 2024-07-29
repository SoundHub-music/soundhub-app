package com.soundhub.ui.pages.authentication

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.soundhub.data.datastore.model.UserPreferences
import com.soundhub.data.datastore.UserCredsStore
import com.soundhub.data.model.User
import com.soundhub.ui.viewmodels.UiStateDispatcher
import com.soundhub.Route
import com.soundhub.ui.events.UiEvent
import com.soundhub.data.api.requests.SignInRequestBody
import com.soundhub.data.dao.UserDao
import com.soundhub.data.repository.AuthRepository
import com.soundhub.data.repository.UserRepository
import com.soundhub.utils.lib.AuthValidator
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
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
    private val _authFormState = MutableStateFlow(AuthFormState())
    val authFormState = _authFormState.asStateFlow()

    private val userCredsFlow: Flow<UserPreferences> = userCredsStore.getCreds()

    init { initializeUser() }

    override fun onCleared() {
        super.onCleared()
        Log.d("AuthenticationViewModel", "viewmodel was cleared")
    }

    fun initializeUser() = viewModelScope.launch(Dispatchers.IO) {
        getCurrentUser().collect { currentUser ->
            // TODO: This code adds a parameter with folder name to image url. It will be implemented in the future
            // if (!Regex(Constants.URL_WITH_PARAMS_REGEX).matches(user.avatarUrl ?: ""))
            //     user.avatarUrl = user.avatarUrl + HttpUtils.FOLDER_NAME_PARAM + MediaFolder.AVATAR.folderName
            currentUser?.let {
                userDao.saveUser(currentUser)
                uiStateDispatcher.setAuthorizedUser(currentUser)
            } ?: run {
                val cachedUser: User? = userDao.getCurrentUser()
                uiStateDispatcher.setAuthorizedUser(cachedUser)
            }
        }
    }

    fun logout() = viewModelScope.launch(Dispatchers.IO) {
        val authorizedUser: User? = userDao.getCurrentUser()

        authRepository
            .logout(authorizedUser, userCredsFlow.firstOrNull()?.accessToken)
            .onSuccessWithContext { uiStateDispatcher.sendUiEvent(UiEvent.Navigate(Route.Authentication)) }
            .onFailureWithContext { error ->
                val errorEvent: UiEvent = UiEvent.Error(
                    response = error.errorBody,
                    throwable = error.throwable
                )

                uiStateDispatcher.sendUiEvent(errorEvent)
            }
            .finally { deleteUserData() }
    }

    private suspend fun deleteUserData() {
        userDao.truncateUser()
        uiStateDispatcher.setAuthorizedUser(null)
        userCredsStore.clear()
    }

    private suspend fun getCurrentUser(): Flow<User?> = flow {
        val authorizedUser: User? = userRepository
            .getCurrentUser()
            .onSuccessReturn()

        emit(authorizedUser)
    }

    fun signIn() = viewModelScope.launch(Dispatchers.Main) {
        _authFormState.update { it.copy(isLoading = true) }

        val ( email: String, password: String ) = authFormState.value
        val signInRequestBody = SignInRequestBody(email, password)

        authRepository.signIn(signInRequestBody)
            .onSuccess { response ->
                val postLineRoute = Route.PostLine
                val uiEvent = UiEvent.Navigate(postLineRoute)

                userCredsStore.updateCreds(response.body)
                initializeUser().invokeOnCompletion {
                    viewModelScope.launch(Dispatchers.Main) {
                        uiStateDispatcher.sendUiEvent(uiEvent)
                    }
                }

            }
            .onFailure { error ->
                val errorEvent: UiEvent = UiEvent.Error(error.errorBody, error.throwable)
                uiStateDispatcher.sendUiEvent(errorEvent)
            }
            .finally {
                _authFormState.update { it.copy(isLoading = false) }
            }
    }

    private suspend fun toggleUserOnline() {
        userRepository.toggleUserOnline()
            .onSuccessWithContext { response -> uiStateDispatcher.setAuthorizedUser(response.body) }
            .onFailureWithContext { error ->
                val errorEvent: UiEvent = UiEvent.Error(error.errorBody, error.throwable)
                uiStateDispatcher.sendUiEvent(errorEvent)
            }
    }

    fun updateUserOnlineStatusDelayed(setOnline: Boolean, delayTime: Long = 0) {
        Log.i(
            "AuthenticationViewModel",
            "updateUserOnlineStatusDelayed: " +
                    "user will be ${if (setOnline) "online" else "offline"} in ${delayTime / 1000} seconds"
        )

        viewModelScope.launch(Dispatchers.IO) {
            delay(delayTime)
            userDao.getCurrentUser()?.let { user ->
                if (setOnline != user.isOnline) {
                    toggleUserOnline()
                }
            }
        }
    }

    fun resetAuthFormState() = _authFormState.update { AuthFormState() }

    fun resetRepeatedPassword() = _authFormState.update {
        it.copy(repeatedPassword = "")
    }

    fun setEmail(value: String) = _authFormState.update {
        val isEmailValid = AuthValidator.validateEmail(value)
        it.copy(email = value, isEmailValid = isEmailValid)
    }

    fun setPassword(value: String) = _authFormState.update {
        val isPasswordValid: Boolean = AuthValidator.validatePassword(value)
        var arePasswordsEqual = true

        if (authFormState.value.isRegisterForm)
            arePasswordsEqual = AuthValidator.arePasswordsEqual(
                authFormState.value.repeatedPassword, value
            )

        it.copy(
            password = value,
            isPasswordValid = isPasswordValid,
            arePasswordsEqual = arePasswordsEqual
        )
    }

    fun setRepeatedPassword(value: String) = _authFormState.update {
        val arePasswordsEqual: Boolean = AuthValidator
            .arePasswordsEqual(authFormState.value.password, value)

        it.copy(
            repeatedPassword = value,
            arePasswordsEqual = arePasswordsEqual
        )
    }

    fun setAuthFormType(value: Boolean) = _authFormState.update {
        it.copy(isRegisterForm = value)
    }
}