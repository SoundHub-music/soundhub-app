package com.soundhub.domain.usecases.user

import com.soundhub.data.model.User
import com.soundhub.data.repository.UserRepository
import java.util.UUID
import javax.inject.Inject

class GetUserByIdUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(userId: UUID): User? {
        return userRepository.getUserById(userId)
            .onSuccessReturn()
    }
}