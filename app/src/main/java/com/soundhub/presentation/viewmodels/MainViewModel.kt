package com.soundhub.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.soundhub.Route
import com.soundhub.data.datastore.UserCredsStore
import com.soundhub.data.datastore.UserSettingsStore
import com.soundhub.data.datastore.model.UserPreferences
import com.soundhub.data.datastore.model.UserSettings
import com.soundhub.data.local_database.dao.UserDao
import com.soundhub.domain.model.User
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
	private val userDao: UserDao,
	userSettingsStore: UserSettingsStore,
	userCredsStore: UserCredsStore,
) : ViewModel() {
	private val _startDestination = MutableStateFlow<String>(Route.Authentication.route)
	val startDestination: StateFlow<String> = _startDestination.asStateFlow()
	val userCreds: Flow<UserPreferences> = userCredsStore.getCreds()

	private val _topBarTitle = MutableStateFlow<String?>(null)
	val topBarTitle: StateFlow<String?> = _topBarTitle.asStateFlow()

	val userSettings: Flow<UserSettings> = userSettingsStore.getCreds()

	init {
		viewModelScope.launch(Dispatchers.Main) {
			userCreds.collect { creds -> defineStartDestination(creds) }
		}
	}

	private suspend fun defineStartDestination(creds: UserPreferences) {
		val authorizedUser: User? = userDao.getCurrentUser()
		if (creds.isValid() && authorizedUser != null)
			_startDestination.update { Route.PostLine.route }
		else _startDestination.update { Route.Authentication.route }
	}


	fun setTopBarTitle(value: String?) = _topBarTitle.update {
		value
	}

	fun getTopBarTitle() = _topBarTitle.value
}