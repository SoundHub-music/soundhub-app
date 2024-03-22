package com.soundhub.data.api

import com.soundhub.data.model.User
import com.soundhub.utils.ApiEndpoints
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Path

interface UserApi {
    @GET(ApiEndpoints.SoundHub.currentUser)
    suspend fun getCurrentUser(
        @Header("Authorization") accessToken: String
    ): Response<User?>

    @GET(ApiEndpoints.SoundHub.getUserById)
    suspend fun getUserById(
        @Path("userId") id: String?,
        @Header("Authorization") accessToken: String?
    ): Response<User?>

    @PUT(ApiEndpoints.SoundHub.updateUser)
    @Multipart
    suspend fun updateUserById(
        @Path("userId") id: String?,
        @Part("userDto") user: RequestBody,
        @Part userAvatar: MultipartBody.Part?,
        @Header("Authorization") accessToken: String?
    ): Response<User>
}