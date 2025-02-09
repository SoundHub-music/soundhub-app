package com.soundhub.presentation.pages.authentication

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.soundhub.Route
import com.soundhub.data.api.requests.SignInRequestBody
import com.soundhub.data.datastore.UserCredsStore
import com.soundhub.data.datastore.model.UserPreferences
import com.soundhub.data.local_database.dao.PostDao
import com.soundhub.data.local_database.dao.UserDao
import com.soundhub.domain.events.UiEvent
import com.soundhub.domain.model.User
import com.soundhub.domain.repository.AuthRepository
import com.soundhub.domain.repository.UserRepository
import com.soundhub.presentation.viewmodels.UiStateDispatcher
import com.soundhub.utils.lib.AuthValidator
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthenticationViewModel @Inject constructor(
	private val authRepository: AuthRepository,
	private val uiStateDispatcher: UiStateDispatcher,
	private val userRepository: UserRepository,
	private val userCredsStore: UserCredsStore,
	private val userDao: UserDao,
	private val postDao: PostDao
) : ViewModel() {
	private val _authFormState = MutableStateFlow(AuthFormState())
	val authFormState = _authFormState.asStateFlow()
	var onlineStatusJob: Job? = null

	private val userCredsFlow: Flow<UserPreferences> = userCredsStore.getCreds()

	init {
		viewModelScope.launch {
			initializeUser()
		}
	}

	override fun onCleared() {
		super.onCleared()
		Log.d("AuthenticationViewModel", "viewmodel was cleared")
	}

	suspend fun getAuthorizedUser(): User? {
		return userDao.getCurrentUser()
	}

	suspend fun initializeUser(onSaveUser: suspend () -> Unit = {}) {
		val userCreds = userCredsFlow.firstOrNull()

		var authorizedUser: User? = userRepository
			.getCurrentUser(userCreds)
			.onSuccessReturn()

		with(authorizedUser) {
			this?.let { userDao.saveOrReplaceUser(this) }
			uiStateDispatcher.setAuthorizedUser(this)
		}

		onSaveUser()
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
		postDao.deleteUserPosts()
		uiStateDispatcher.setAuthorizedUser(null)
		userCredsStore.clear()
	}

	fun signIn() = viewModelScope.launch(Dispatchers.Main) {
		_authFormState.update { it.copy(isLoading = true) }

		val (email: String, password: String) = authFormState.value
		val signInRequestBody = SignInRequestBody(email, password)

		authRepository.signIn(signInRequestBody)
			.onSuccess { response ->
				userCredsStore.updateCreds(response.body)
				initializeUser {
					val postLineRoute = Route.PostLine
					val uiEvent = UiEvent.Navigate(postLineRoute)
					uiStateDispatcher.sendUiEvent(uiEvent)
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

	private fun updateUserOnline(online: Boolean) = viewModelScope.launch(Dispatchers.IO) {
		userRepository.updateUserOnline(online)
			.onSuccess { response ->
				val user: User? = response.body

				Log.d(
					"AuthenticationViewModel",
					"updateUserOnline: user online = ${user?.online}"
				)

				if (user != null) {
					userDao.saveOrReplaceUser(user)
					uiStateDispatcher.setAuthorizedUser(user)
				}
			}
			.onFailure { error ->
				val errorEvent: UiEvent = UiEvent.Error(error.errorBody, error.throwable)
				uiStateDispatcher.sendUiEvent(errorEvent)
			}
	}

	fun updateUserOnlineStatusDelayed(online: Boolean, delayTime: Long = 0) {
		if (onlineStatusJob != null && onlineStatusJob?.isCompleted == false) {
			onlineStatusJob?.cancel()
			onlineStatusJob = null
		}

		Log.i(
			"AuthenticationViewModel",
			"updateUserOnlineStatusDelayed[1]: " +
					"user will be ${if (online) "online" else "offline"} in ${delayTime / 1000} seconds"
		)

		onlineStatusJob = viewModelScope.launch(Dispatchers.IO) {
			delay(delayTime)
			getAuthorizedUser()?.let { user ->
				if (online != user.online) {
					Log.d(
						"AuthenticationViewModel",
						"updateUserOnlineStatusDelayed[2]: online status is setting to $online"
					)
					updateUserOnline(online)
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