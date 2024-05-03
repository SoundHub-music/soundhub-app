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
import com.soundhub.domain.usecases.file.GetImageUseCase
import com.soundhub.ui.authentication.states.AuthFormState
import com.soundhub.ui.states.UiState
import com.soundhub.utils.MediaFolder
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
    private val getImageUseCase: GetImageUseCase,
    appDb: AppDatabase
) : ViewModel() {
    private val uiState: Flow<UiState> = uiStateDispatcher.uiState.asStateFlow()
    private val userDao: UserDao = appDb.userDao()
    private val authAttemptCount: MutableStateFlow<Int> = MutableStateFlow(0)
    private val maxRefreshTokenAttemptCount: Int = 2

    val userCreds: Flow<UserPreferences> = userCredsStore.getCreds()
    val authFormState = MutableStateFlow(AuthFormState())

    init {
        viewModelScope.launch {
            val creds = userCreds.firstOrNull()
            initializeUser(creds)
        }
    }

    override fun onCleared() {
        super.onCleared()
        Log.d("AuthenticationViewModel", "viewmodel was cleared")
    }

    private suspend fun initializeUser(creds: UserPreferences?) {
        if (!creds?.accessToken.isNullOrEmpty()) {
            getCurrentUser().collect { currentUser ->
                currentUser?.let { userDao.saveUser(currentUser) }
                uiStateDispatcher.setAuthorizedUser(currentUser)
                loadAuthenticatedUserAvatar()
            }
        }
    }


    private suspend fun loadAuthenticatedUserAvatar() {
        val authorizedUser: User? = uiState
            .firstOrNull()
            ?.authorizedUser

        val avatar = getImageUseCase(
            accessToken = userCreds.firstOrNull()?.accessToken,
            fileName = authorizedUser?.avatarUrl,
            folderName = MediaFolder.Avatar.NAME
        )
        Log.d("AuthenticationViewModel", "user avatar: ${avatar?.name.toString()}")

        val currentUser = authorizedUser?.copy()
        currentUser?.avatarImageFile = avatar

        uiStateDispatcher.setAuthorizedUser(currentUser)
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

    fun logout() = viewModelScope.launch {
        Log.d("AuthenticationViewModel", "logout: $userCreds")
        userCreds.collect { creds ->
            authRepository
                .logout(creds.accessToken)
                .onFailure {
                    uiStateDispatcher.sendUiEvent(
                        UiEvent.ShowToast(
                        UiText.DynamicString(it.errorBody.detail ?: "")
                    ))
                }
                .finally {
                    val currentUser: User? = uiState
                        .firstOrNull()
                        ?.authorizedUser
                        ?.copy()

                    currentUser?.let {
                        userDao.deleteUser(it)
                        it.avatarImageFile?.delete()
                        uiStateDispatcher.setAuthorizedUser(null)
                    }
                    userCredsStore.clear()
                }
        }
    }

    private suspend fun getCurrentUser(): Flow<User?> = flow {
        val creds = userCreds.firstOrNull()
        creds?.accessToken?.let {
            userRepository.getCurrentUser(creds.accessToken)
            .onSuccess { emit(it.body) }
            .onFailure { currentUserError ->
                tryRefreshToken(
                    error = currentUserError,
                    userCreds = creds
                )
            }
        }
    }

    private suspend fun tryRefreshToken(
        error: HttpResult.Error<User?>,
        userCreds: UserPreferences?
    ) {
        authRepository.refreshToken(
            RefreshTokenRequestBody(userCreds?.refreshToken)
        ).onSuccess { newCredsResponse ->
            userCredsStore.updateCreds(newCredsResponse.body)
            initializeUser(userCreds)
            authAttemptCount.update { 0 }

        }.onFailure {
            authAttemptCount.update { it + 1 }
            if (authAttemptCount.value <= maxRefreshTokenAttemptCount)
                tryRefreshToken(error, userCreds)
            else {
                uiStateDispatcher.sendUiEvent(
                    UiEvent.ShowToast(
                        UiText.DynamicString(error.errorBody.detail ?: "")
                    )
                )
                uiStateDispatcher.sendUiEvent(UiEvent.Navigate(Route.Authentication))
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

                uiStateDispatcher.setAuthorizedUser(currentUser)
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