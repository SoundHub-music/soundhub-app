package com.soundhub.data.api

import com.soundhub.data.model.User
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path

interface UserApi {
    @GET("users/currentUser")
    suspend fun getCurrentUser(@Header("Authorization") accessToken: String): Response<User?>

    @GET("users/{userId}")
    suspend fun getUserById(
        @Path("userId") id: String,
        @Header("Authorization") accessToken: String?
    ): Response<User?>
}