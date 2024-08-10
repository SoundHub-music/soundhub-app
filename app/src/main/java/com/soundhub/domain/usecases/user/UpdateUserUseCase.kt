package com.soundhub.domain.usecases.user

import com.soundhub.data.model.User
import com.soundhub.data.repository.UserRepository
import javax.inject.Inject

class UpdateUserUseCase @Inject constructor(
	private val userRepository: UserRepository
) {
	suspend operator fun invoke(user: User?) = runCatching {
		userRepository
			.updateUserById(user)
			.onFailure { error ->
				error.throwable?.let { throw error.throwable }
					?: throw Exception(error.errorBody.detail)
			}
	}
}