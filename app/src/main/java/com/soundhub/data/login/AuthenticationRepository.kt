package com.soundhub.data.login

interface AuthenticationRepository {
    suspend fun login(email: String, password: String);
    suspend fun register(email: String, password: String);
}