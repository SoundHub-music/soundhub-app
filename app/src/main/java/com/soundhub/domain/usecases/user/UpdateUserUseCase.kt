package com.soundhub.domain.usecases.user

import android.util.Log
import com.soundhub.data.enums.ApiStatus
import com.soundhub.data.model.User
import com.soundhub.data.repository.UserRepository
import javax.inject.Inject

class UpdateUserUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(user: User?, accessToken: String?): ApiStatus {
        return userRepository
            .updateUserById(accessToken, user)
            .onFailure {
                Log.e("UpdateUserUseCase", "update user error: $it")
            }.status
    }
}