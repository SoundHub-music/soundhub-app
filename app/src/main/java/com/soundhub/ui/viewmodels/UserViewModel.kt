package com.soundhub.ui.viewmodels

import androidx.lifecycle.ViewModel
import com.soundhub.data.datastore.UserPreferences
import com.soundhub.data.datastore.UserCredsStore
import com.soundhub.data.model.User
import com.soundhub.data.api.responses.HttpResult
import com.soundhub.data.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(
    private val userRepository: UserRepository,
    userCredsStore: UserCredsStore,
): ViewModel() {
    private var userCreds: Flow<UserPreferences?> = userCredsStore.getCreds()

    fun getUserById(id: UUID): Flow<User?> = flow {
        userCreds.collect { creds ->
            val userResponse: HttpResult<User?> = userRepository.getUserById(id, creds?.accessToken)
            userResponse
                .onSuccess { emit(it.body) }
                .onFailure { emit(null) }
        }
    }
}