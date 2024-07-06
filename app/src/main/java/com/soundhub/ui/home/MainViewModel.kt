package com.soundhub.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.soundhub.Route
import com.soundhub.data.dao.UserDao
import com.soundhub.data.datastore.UserCredsStore
import com.soundhub.data.datastore.UserPreferences
import com.soundhub.data.model.User
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
    private val gson: Gson,
    userCredsStore: UserCredsStore
): ViewModel() {
    private val _startDestination = MutableStateFlow<String>(Route.Authentication.route)
    val startDestination: StateFlow<String> = _startDestination.asStateFlow()
    val userCreds: Flow<UserPreferences> = userCredsStore.getCreds()

    init {
        viewModelScope.launch(Dispatchers.Main) {
            userCreds.collect { creds -> defineStartDestination(creds) }
        }
    }

    private suspend fun defineStartDestination(creds: UserPreferences) {
        val authorizedUser: User? = userDao.getCurrentUser()
        if (
            !creds.refreshToken.isNullOrEmpty() &&
            !creds.accessToken.isNullOrEmpty() &&
            authorizedUser != null
        )
            _startDestination.update { Route.PostLine.route }
        else _startDestination.update { Route.Authentication.route }
    }

    fun getGson(): Gson = gson
}