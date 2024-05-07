package com.soundhub.domain.usecases.user

import com.soundhub.data.datastore.UserCredsStore
import com.soundhub.data.datastore.UserPreferences
import com.soundhub.data.model.User
import com.soundhub.data.repository.UserRepository
import kotlinx.coroutines.flow.firstOrNull
import java.util.UUID
import javax.inject.Inject

class GetUserByIdUseCase @Inject constructor(
    private val userRepository: UserRepository,
    private val userCredsStore: UserCredsStore
) {
    suspend operator fun invoke(userId: UUID): User? {
        val userCreds: UserPreferences? = userCredsStore.getCreds()
            .firstOrNull()

        return userRepository.getUserById(userCreds?.accessToken, userId)
            .onSuccessReturn()
    }
}