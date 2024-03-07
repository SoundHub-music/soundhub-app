package com.soundhub.viewmodels

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.soundhub.data.datastore.UserPreferences
import com.soundhub.data.datastore.UserStore
import com.soundhub.data.model.User
import com.soundhub.data.model.ApiResult
import com.soundhub.data.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(
    private val userRepository: UserRepository,
    userStore: UserStore
): ViewModel() {
    private var userCreds: MutableStateFlow<UserPreferences?> = MutableStateFlow(null)
    init {
        viewModelScope.launch {
            userCreds.update { userStore.getCreds().firstOrNull() }
        }
    }

    fun getUserById(id: String, user: MutableState<User?>) = viewModelScope.launch(Dispatchers.IO) {
        try {
            val userResponse: ApiResult<User?> = userRepository.getUserById(id, userCreds.value?.accessToken)
            user.value = userResponse.data
        }
        catch (e: Exception) {
            Log.e("fetchUserError", "error ${e.message}")
        }
    }

}