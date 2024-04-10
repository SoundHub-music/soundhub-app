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

interface UserService {
    @GET(ApiEndpoints.SoundHub.CURRENT_USER)
    suspend fun getCurrentUser(
        @Header("Authorization") accessToken: String?
    ): Response<User?>

    @GET(ApiEndpoints.SoundHub.GET_USER_BY_ID)
    suspend fun getUserById(
        @Path("userId") id: String?,
        @Header("Authorization") accessToken: String?
    ): Response<User?>

    @PUT(ApiEndpoints.SoundHub.UPDATE_USER)
    @Multipart
    suspend fun updateUserById(
        @Path("userId") id: String?,
        @Part("userDto") user: RequestBody,
        @Part userAvatar: MultipartBody.Part?,
        @Header("Authorization") accessToken: String?
    ): Response<User>
}