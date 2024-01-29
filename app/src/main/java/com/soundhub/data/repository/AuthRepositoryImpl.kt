package com.soundhub.data.repository

import com.soundhub.data.UserDao
import com.soundhub.data.model.User

class AuthRepositoryImpl(private val dao: UserDao): AuthRepository {
    override suspend fun login(email: String, password: String): User? {
        return dao.login(email, password)
    }

    override suspend fun register(user: User) {
        dao.register(user)
    }
}