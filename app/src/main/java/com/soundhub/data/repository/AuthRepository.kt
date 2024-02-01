package com.soundhub.data.repository

import com.soundhub.data.model.User

interface AuthRepository {
//    @GET("auth/login/{password}")
//    suspend fun login(@Path("password") password: String): Response<User>
//
//    @POST("auth/register")
//    suspend fun register(user: User)

    suspend fun login(email: String, password: String): User?
    suspend fun register(user: User)
}