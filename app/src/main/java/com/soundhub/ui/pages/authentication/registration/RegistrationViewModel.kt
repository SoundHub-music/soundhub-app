package com.soundhub.ui.pages.authentication.registration

import android.net.Uri
import android.util.Log
import androidx.lifecycle.viewModelScope
import com.soundhub.R
import com.soundhub.Route
import com.soundhub.Route.Authentication
import com.soundhub.data.api.requests.RegisterRequestBody
import com.soundhub.data.dao.UserDao
import com.soundhub.data.datastore.UserCredsStore
import com.soundhub.data.datastore.model.UserPreferences
import com.soundhub.data.enums.Gender
import com.soundhub.data.model.User
import com.soundhub.data.repository.AuthRepository
import com.soundhub.data.states.RegistrationState
import com.soundhub.domain.usecases.music.LoadArtistsUseCase
import com.soundhub.domain.usecases.music.LoadGenresUseCase
import com.soundhub.domain.usecases.music.SearchArtistsUseCase
import com.soundhub.ui.events.UiEvent
import com.soundhub.ui.pages.authentication.AuthFormState
import com.soundhub.ui.shared.forms.IUserDataFormState
import com.soundhub.ui.viewmodels.BaseMusicPreferencesViewModel
import com.soundhub.ui.viewmodels.UiStateDispatcher
import com.soundhub.utils.lib.AuthValidator
import com.soundhub.utils.lib.UiText
import com.soundhub.utils.mappers.RegisterDataMapper
import com.soundhub.utils.mappers.UserMapper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class RegistrationViewModel @Inject constructor(
	private val uiStateDispatcher: UiStateDispatcher,
	private val authRepository: AuthRepository,
	private val userCredsStore: UserCredsStore,
	private val userDao: UserDao,
	loadGenresUseCase: LoadGenresUseCase,
	loadArtistsUseCase: LoadArtistsUseCase,
	searchArtistsUseCase: SearchArtistsUseCase,
) : BaseMusicPreferencesViewModel(
	loadGenresUseCase,
	loadArtistsUseCase,
	searchArtistsUseCase,
	uiStateDispatcher
) {
	private val uiState = uiStateDispatcher.uiState
	private val _registerState = MutableStateFlow(RegistrationState())
	val registerState: Flow<IUserDataFormState> = _registerState.asStateFlow()

	override fun onCleared() {
		super.onCleared()
		Log.d("PostRegistrationViewModel", "viewmodel was cleared")
	}

	override fun onNextButtonClick() = viewModelScope.launch(Dispatchers.Main) {
		Log.d("RegistrationViewModel", "onPostRegisterNextButtonClick: ${_registerState.value}")
		val uiState = uiState.firstOrNull()
		when (uiState?.currentRoute) {
			Authentication.ChooseGenres.route -> onChooseGenresNextButtonClick()
			Authentication.ChooseArtists.route -> onChooseArtistsNextButtonClick()
			Authentication.FillUserData.route -> handleFillUserData()
			else -> Unit
		}
	}

	override fun onChooseGenresNextButtonClick() = viewModelScope.launch(Dispatchers.Main) {
		_registerState.update {
			it.copy(favoriteGenres = _genreUiState.value.chosenGenres)
		}

		loadArtists()
		uiStateDispatcher.sendUiEvent(UiEvent.Navigate(Authentication.ChooseArtists))
	}

	override fun onChooseArtistsNextButtonClick() = viewModelScope.launch(Dispatchers.Main) {
		if (_artistUiState.value.chosenArtists.isEmpty()) {
			val toastEvent =
				UiEvent.ShowToast(UiText.StringResource(R.string.choose_artist_warning))
			uiStateDispatcher.sendUiEvent(toastEvent)
			return@launch
		}

		_registerState.update {
			it.copy(favoriteArtists = _artistUiState.value.chosenArtists)
		}

		uiStateDispatcher.sendUiEvent(UiEvent.Navigate(Authentication.FillUserData))
	}

	private fun handleFillUserData() = viewModelScope.launch(Dispatchers.Main) {
		loadArtistsJob?.cancel()
		searchJob?.cancel()

		_registerState.update {
			it.copy(
				isFirstNameValid = it.firstName.isNotEmpty(),
				isLastNameValid = it.lastName.isNotEmpty(),
				isBirthdayValid = it.birthday != null
			)
		}

		if (AuthValidator.validateRegistrationState(_registerState.value)) {
			val user: User = UserMapper.impl.fromRegistrationState(_registerState.value)
			handleRegister(user)
		}
	}


	fun onSignUpButtonClick(authForm: AuthFormState) = viewModelScope.launch(Dispatchers.Main) {
		Log.d("PostRegistrationViewModel", "authForm: $authForm")
		_registerState.update {
			it.copy(
				email = authForm.email,
				password = authForm.password
			)
		}
		val uiEvent = UiEvent.Navigate(Authentication.ChooseGenres)
		uiStateDispatcher.sendUiEvent(uiEvent)
	}


	private fun handleRegister(user: User) = viewModelScope.launch(Dispatchers.IO) {
		val registerRequestBody: RegisterRequestBody = RegisterDataMapper.impl
			.registerStateToRegisterRequestBody(_registerState.value)

		authRepository
			.signUp(registerRequestBody)
			.onSuccess { response -> saveUserCreds(response.body, user) }
			.onFailureWithContext { error ->
				val errorEvent: UiEvent = UiEvent.Error(error.errorBody, error.throwable)
				uiStateDispatcher.sendUiEvent(errorEvent)
			}
	}

	private suspend fun saveUserCreds(
		userCreds: UserPreferences?,
		user: User
	) {
		userCredsStore.updateCreds(userCreds)
		userDao.saveOrReplaceUser(user)

		with(uiStateDispatcher) {
			setAuthorizedUser(user)
			sendUiEvent(UiEvent.Navigate(Route.PostLine))
		}
	}

	fun setFirstName(value: String) = _registerState.update {
		it.copy(firstName = value, isFirstNameValid = value.isNotEmpty())
	}


	fun setLastName(value: String) = _registerState.update {
		it.copy(lastName = value, isLastNameValid = value.isNotEmpty())
	}


	fun setBirthday(value: LocalDate?) = _registerState.update {
		it.copy(birthday = value, isBirthdayValid = value != null)
	}


	fun setGender(value: String) {
		try {
			_registerState.update { it.copy(gender = Gender.valueOf(value)) }
		} catch (e: IllegalArgumentException) {
			Log.e("RegistrationViewModel", "setGender: ${e.message}")
			_registerState.update {
				it.copy(gender = Gender.UNKNOWN)
			}
		}
	}

	fun setCountry(value: String) = _registerState.update { it.copy(country = value) }
	fun setCity(value: String) = _registerState.update { it.copy(city = value) }
	fun setDescription(value: String) = _registerState.update { it.copy(description = value) }

	fun setLanguages(languages: List<String>) = _registerState.update {
		it.copy(languages = languages.toMutableList())
	}

	fun setAvatar(avatarUrl: Uri?) = _registerState.update {
		it.copy(avatarUrl = avatarUrl?.toString())
	}
}