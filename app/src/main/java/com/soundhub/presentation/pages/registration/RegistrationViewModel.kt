package com.soundhub.presentation.pages.registration

import android.net.Uri
import android.util.Log
import androidx.lifecycle.viewModelScope
import com.soundhub.R
import com.soundhub.Route
import com.soundhub.Route.Authentication
import com.soundhub.data.api.requests.RegisterRequestBody
import com.soundhub.data.datastore.UserCredsStore
import com.soundhub.data.datastore.model.UserPreferences
import com.soundhub.data.local_database.dao.UserDao
import com.soundhub.domain.enums.Gender
import com.soundhub.domain.events.UiEvent
import com.soundhub.domain.model.User
import com.soundhub.domain.repository.AuthRepository
import com.soundhub.domain.repository.UserRepository
import com.soundhub.domain.states.RegistrationState
import com.soundhub.domain.states.interfaces.IUserDataFormState
import com.soundhub.domain.usecases.music.LoadArtistsUseCase
import com.soundhub.domain.usecases.music.LoadGenresUseCase
import com.soundhub.domain.usecases.music.SearchArtistsUseCase
import com.soundhub.presentation.pages.authentication.AuthFormState
import com.soundhub.presentation.shared.forms.FormHandler
import com.soundhub.presentation.shared.forms.IUserFormViewModel
import com.soundhub.presentation.viewmodels.BaseMusicPreferencesViewModel
import com.soundhub.presentation.viewmodels.UiStateDispatcher
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
	private val userRepository: UserRepository,
	private val userDao: UserDao,
	loadGenresUseCase: LoadGenresUseCase,
	loadArtistsUseCase: LoadArtistsUseCase,
	searchArtistsUseCase: SearchArtistsUseCase,
) : BaseMusicPreferencesViewModel(
	loadGenresUseCase,
	loadArtistsUseCase,
	searchArtistsUseCase,
	uiStateDispatcher
), IUserFormViewModel {
	private val uiState = uiStateDispatcher.uiState
	private val _registerState = MutableStateFlow(RegistrationState())
	val registerState: Flow<IUserDataFormState> = _registerState.asStateFlow()

	override val formHandler = FormHandler(
		onDescriptionChange = ::setDescription,
		onLanguagesChange = ::setLanguages,
		onFirstNameChange = ::setFirstName,
		onLastNameChange = ::setLastName,
		onBirthdayChange = ::setBirthday,
		onCountryChange = ::setCountry,
		onGenderChange = ::setGender,
		onAvatarChange = ::setAvatar,
		onCityChange = ::setCity,
	)

	init {
		loadGenres()
	}

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
		if (_genreUiState.value.chosenGenres.isEmpty()) {
			val toastEvent =
				UiEvent.ShowToast(UiText.StringResource(R.string.choose_artist_warning))

			uiStateDispatcher.sendUiEvent(toastEvent)
			return@launch
		}

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
		try {
			loadArtistsJob?.cancel()
			searchJob?.cancel()
		} catch (e: Exception) {
			Log.e("RegistrationViewModel", "handleFillUserData: ${e.localizedMessage}")
		}

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


	fun onSignUpButtonClick(authForm: AuthFormState) = viewModelScope.launch {
		Log.d("PostRegistrationViewModel", "authForm: $authForm")
		var uiEvent: UiEvent = UiEvent.Navigate(Authentication.ChooseGenres)

		userRepository.checkUserExistenceByEmail(authForm.email).onSuccess { response ->
			val isUserExists = response.body?.isUserExists == true

			if (!isUserExists) {
				_registerState.update {
					it.copy(
						email = authForm.email,
						password = authForm.password
					)
				}
			} else {
				val message = UiText.StringResource(
					R.string.toast_user_with_such_email_already_exists
				)

				uiEvent = UiEvent.ShowToast(message)
			}

		}.onFailure {
			val message = UiText.StringResource(R.string.toast_registration_error)
			uiEvent = UiEvent.ShowToast(message)
		}
			.finally { uiStateDispatcher.sendUiEvent(uiEvent) }
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

	override fun setFirstName(value: String) = _registerState.update {
		it.copy(firstName = value, isFirstNameValid = value.isNotEmpty())
	}

	override fun setLastName(value: String) = _registerState.update {
		it.copy(lastName = value, isLastNameValid = value.isNotEmpty())
	}

	override fun setBirthday(value: LocalDate?) = _registerState.update {
		it.copy(birthday = value, isBirthdayValid = value != null)
	}

	override fun setGender(value: String) {
		try {
			_registerState.update { it.copy(gender = Gender.valueOf(value)) }
		} catch (e: IllegalArgumentException) {
			Log.e("RegistrationViewModel", "setGender: ${e.message}")
			_registerState.update {
				it.copy(gender = Gender.UNKNOWN)
			}
		}
	}

	override fun setCountry(value: String) = _registerState.update { it.copy(country = value) }
	override fun setCity(value: String) = _registerState.update { it.copy(city = value) }
	override fun setDescription(value: String) =
		_registerState.update { it.copy(description = value) }

	override fun setLanguages(languages: List<String>) = _registerState.update {
		it.copy(languages = languages.toMutableList())
	}

	override fun setAvatar(avatarUrl: Uri?) = _registerState.update {
		it.copy(avatarUrl = avatarUrl?.toString())
	}
}