package com.soundhub.domain.usecases.user

import com.soundhub.data.dao.UserDao
import com.soundhub.data.database.AppDatabase
import javax.inject.Inject

class UpdateAuthorizedUserUseCase @Inject constructor(
    appDb: AppDatabase
) {
    private val userDao: UserDao = appDb.userDao()

    suspend operator fun invoke() {

    }
}