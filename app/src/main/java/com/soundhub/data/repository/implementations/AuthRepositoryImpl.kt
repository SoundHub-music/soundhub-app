package com.soundhub.data.repository.implementations

import com.soundhub.data.dao.UserDao
import com.soundhub.data.model.User
import com.soundhub.data.repository.AuthRepository


// TODO: delete room db logic and implement rest api
class AuthRepositoryImpl(private val dao: UserDao): AuthRepository {
    override suspend fun login(email: String, password: String): User? {
        return dao.login(email, password)
    }

    override suspend fun register(user: User) {
        dao.register(user)
    }
}