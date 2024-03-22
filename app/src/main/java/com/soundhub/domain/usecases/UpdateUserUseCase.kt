package com.soundhub.domain.usecases

import android.util.Log
import com.soundhub.data.model.User
import com.soundhub.data.repository.UserRepository
import javax.inject.Inject

class UpdateUserUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(user: User?, accessToken: String?) {
        userRepository.updateUserById(user, accessToken)
            .onFailure {
                Log.d("UpdateUserUseCase", it.errorBody?.detail ?: "")
            }
    }
}