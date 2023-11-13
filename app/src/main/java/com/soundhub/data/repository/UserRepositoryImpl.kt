package com.soundhub.data.repository

import com.soundhub.data.UserDao
import com.soundhub.data.model.User

class UserRepositoryImpl(private val dao: UserDao): UserRepository {
    override suspend fun login(email: String, password: String) {
        dao.login(email, password)
    }

    override suspend fun registerUser(user: User) {
        dao.registerUser(user)
    }
}