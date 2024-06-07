package com.soundhub.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.soundhub.Route
import com.soundhub.data.datastore.UserCredsStore
import com.soundhub.data.datastore.UserPreferences
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    userCredsStore: UserCredsStore
): ViewModel() {
    private val _startDestination = MutableStateFlow<String>(Route.Authentication.route)
    val startDestination: StateFlow<String> = _startDestination.asStateFlow()
    val userCreds: Flow<UserPreferences> = userCredsStore.getCreds()

    init {
        viewModelScope.launch {
            userCreds.collect { creds ->
                defineStartDestination(creds)
            }
        }
    }

    private fun defineStartDestination(creds: UserPreferences): String =
        if (creds.refreshToken.isNullOrEmpty())
               Route.Authentication.route
            else Route.PostLine.route
}