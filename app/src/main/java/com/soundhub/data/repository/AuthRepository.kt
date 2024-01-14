package com.soundhub.data.repository

import com.soundhub.data.model.User

interface AuthRepository {
    suspend fun login(email: String, password: String): User?
    suspend fun registerUser(user: User)
}