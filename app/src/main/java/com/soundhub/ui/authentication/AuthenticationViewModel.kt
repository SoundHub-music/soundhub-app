package com.soundhub.ui.authentication

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.soundhub.data.datastore.UserPreferences
import com.soundhub.data.datastore.UserCredsStore
import com.soundhub.data.model.User
import com.soundhub.ui.viewmodels.UiStateDispatcher
import com.soundhub.Route
import com.soundhub.UiEvent
import com.soundhub.data.api.requests.RefreshTokenRequestBody
import com.soundhub.data.api.requests.SignInRequestBody
import com.soundhub.data.api.responses.HttpResult
import com.soundhub.data.enums.ApiStatus
import com.soundhub.data.repository.AuthRepository
import com.soundhub.data.repository.UserRepository
import com.soundhub.domain.usecases.GetImageUseCase
import com.soundhub.ui.authentication.states.AuthFormState
import com.soundhub.ui.authentication.states.UserState
import com.soundhub.utils.UiText
import com.soundhub.utils.Validator
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

@HiltViewModel
class AuthenticationViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val uiStateDispatcher: UiStateDispatcher,
    private val userRepository: UserRepository,
    private val userCredsStore: UserCredsStore,
    private val getImageUseCase: GetImageUseCase
) : ViewModel() {
    val userCreds: Flow<UserPreferences> = userCredsStore.getCreds()
    var userInstance: MutableStateFlow<UserState> = MutableStateFlow(UserState())
        private set
    var authFormState = MutableStateFlow(AuthFormState())
        private set
    var currentUserAvatar = MutableStateFlow<File?>(null)
        private set

    private val getAuthorizedUserAttempts = MutableStateFlow(0)

    init {
        viewModelScope.launch {
            userCreds.collect { creds ->
                if (!creds.accessToken.isNullOrEmpty()) {
                    getCurrentUser().collect { currentUser ->
                        val status = if (currentUser != null) ApiStatus.SUCCESS
                            else ApiStatus.ERROR
                        userInstance.update {
                            it.copy(
                                current = currentUser,
                                status = status
                            )
                        }
                    }
                    loadAuthenticatedUserAvatar()
                }
                else userInstance.update { it.copy(status = ApiStatus.ERROR) }
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        Log.d("AuthenticationViewModel", "viewmodel was cleared")
    }

    private suspend fun loadAuthenticatedUserAvatar() {
        if (userInstance.value.current?.avatarUrl != null) {
            val avatar = getImageUseCase(
                accessToken = userCreds.firstOrNull()?.accessToken,
                fileName = userInstance.value.current?.avatarUrl,
                folderName = "avatars"
            )
            Log.d("AuthenticationViewModel", "user avatar: ${avatar?.name.toString()}")
            currentUserAvatar.value = avatar
        }
    }

    fun resetAuthFormState() = authFormState.update { AuthFormState() }

    fun resetRepeatedPassword() = authFormState.update {
        authFormState.value.copy(repeatedPassword = null)
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

    fun setCurrentUser(user: User) = userInstance.update {
        it.copy(current = user)
    }


    fun logout() = viewModelScope.launch {
        Log.d("AuthenticationViewModel", "logout: $userCreds")
        val tokens = userCreds.firstOrNull()
        authRepository
            .logout(tokens?.accessToken)
            .onFailure {
            uiStateDispatcher.sendUiEvent(UiEvent.ShowToast(
                UiText.DynamicString(it.errorBody.detail ?: "")
            ))
        }

        userCredsStore.clear()
    }

    private suspend fun getCurrentUser(): Flow<User?> = flow {
        val creds = userCreds.firstOrNull()
        userRepository.getCurrentUser(creds?.accessToken)
        .onSuccess { emit(it.body) }
        .onFailure { currentUserError ->
            tryRefreshToken(
                error = currentUserError,
                userCreds = creds
            )
        }
    }

    private suspend fun tryRefreshToken(
        error: HttpResult.Error<User?>,
        userCreds: UserPreferences?
    ): Flow<User?> = flow {
        val newCreds: HttpResult<UserPreferences?> = authRepository.refreshToken(
            RefreshTokenRequestBody(userCreds?.refreshToken)
        )

        newCreds.onSuccess { newCredsResponse ->
            userCredsStore.updateCreds(newCredsResponse.body)
            uiStateDispatcher.sendUiEvent(
                UiEvent.ShowToast(
                    UiText.DynamicString(error.errorBody.detail ?: "")
                )
            )
            getAuthorizedUserAttempts.update { 0 }

        }.onFailure {
            getAuthorizedUserAttempts.update { it + 1 }
            if (getAuthorizedUserAttempts.value > 1) {
                uiStateDispatcher.sendUiEvent(UiEvent.Navigate(Route.Authentication))
                emit(null)
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
                val currentUser: User? = getCurrentUser()
                    .firstOrNull()

                userInstance.update {
                    it.copy(
                        current = currentUser,
                        status = ApiStatus.SUCCESS
                    )
                }
                uiStateDispatcher.sendUiEvent(UiEvent.Navigate(Route.Postline))
            }
            .onFailure {
                uiStateDispatcher.sendUiEvent(
                    UiEvent.ShowToast(
                        UiText.DynamicString(
                            it.errorBody.detail ?: ""
                        )
                    )
                )
            }

        authFormState.update { it.copy(isLoading = false) }
    }
}