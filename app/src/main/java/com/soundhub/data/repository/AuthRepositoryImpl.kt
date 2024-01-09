package com.soundhub.data.repository

import com.soundhub.data.UserDao
import com.soundhub.data.model.User

class AuthRepositoryImpl(private val dao: UserDao): AuthRepository {
    override suspend fun login(email: String) {
        dao.login(email)
    }

    override suspend fun registerUser(user: User) {
        dao.registerUser(user)
    }
}