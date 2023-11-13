package com.soundhub.data.repository

import com.soundhub.data.model.User

interface UserRepository {
    suspend fun login(email: String, password: String)
    suspend fun registerUser(user: User)
}