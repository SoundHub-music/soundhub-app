package com.soundhub.data.api

import com.soundhub.data.api.requests.RefreshTokenRequestBody
import com.soundhub.data.api.requests.SignInRequestBody
import com.soundhub.data.api.responses.LogoutResponse
import com.soundhub.data.datastore.UserPreferences
import com.soundhub.utils.ApiEndpoints
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface AuthService {
    @POST(ApiEndpoints.SoundHub.signUp)
    @Multipart
    suspend fun signUp(
        @Part(value = "userData") userData: RequestBody,
        @Part file: MultipartBody.Part?
    ): Response<UserPreferences>

    @POST(ApiEndpoints.SoundHub.signIn)
    suspend fun signIn(
        @Body userData: SignInRequestBody
    ): Response<UserPreferences>

    @POST(ApiEndpoints.SoundHub.logout)
    suspend fun logout(
        @Header("Authorization") token: String?
    ): Response<LogoutResponse>

    @POST(ApiEndpoints.SoundHub.refreshToken)
    suspend fun refreshToken(
        @Body refreshToken: RefreshTokenRequestBody
    ): Response<UserPreferences>

}