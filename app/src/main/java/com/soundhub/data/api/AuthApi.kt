package com.soundhub.data.api

import com.soundhub.data.api.requests.RefreshTokenRequestBody
import com.soundhub.data.api.requests.RegisterRequestBody
import com.soundhub.data.api.requests.SignInRequestBody
import com.soundhub.data.api.responses.LogoutResponse
import com.soundhub.data.datastore.UserPreferences
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface AuthApi {
    @POST("auth/sign-up")
    suspend fun signUp(
        @Body userData: RegisterRequestBody
    ): Response<UserPreferences>

    @POST("auth/sign-in")
    suspend fun signIn(
        @Body userData: SignInRequestBody
    ): Response<UserPreferences>

    @POST("auth/logout")
    suspend fun logout(
        @Header("Authorization") token: String?
    ): Response<LogoutResponse>

    @POST("auth/refresh")
    suspend fun refreshToken(
        @Body refreshToken: RefreshTokenRequestBody
    ): Response<UserPreferences>

}