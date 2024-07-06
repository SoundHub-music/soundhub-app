package com.soundhub.data.api.services

import com.soundhub.data.api.requests.RefreshTokenRequestBody
import com.soundhub.data.api.requests.SignInRequestBody
import com.soundhub.data.api.responses.LogoutResponse
import com.soundhub.data.datastore.UserPreferences
import com.soundhub.utils.ApiEndpoints.Authentication.LOGOUT
import com.soundhub.utils.ApiEndpoints.Authentication.REFRESH_TOKEN
import com.soundhub.utils.ApiEndpoints.Authentication.SIGN_IN
import com.soundhub.utils.ApiEndpoints.Authentication.SIGN_UP
import com.soundhub.utils.ApiEndpoints.Authentication.USER_DATA_REQUEST_BODY_NAME
import com.soundhub.utils.HttpUtils.Companion.AUTHORIZATION_HEADER
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface AuthService {
    @POST(SIGN_UP)
    @Multipart
    suspend fun signUp(
        @Part(USER_DATA_REQUEST_BODY_NAME) userData: RequestBody,
        @Part file: MultipartBody.Part?
    ): Response<UserPreferences>

    @POST(SIGN_IN)
    suspend fun signIn(
        @Body userData: SignInRequestBody
    ): Response<UserPreferences>

    @POST(LOGOUT)
    suspend fun logout(
        @Header(AUTHORIZATION_HEADER)
        accessToken: String?
    ): Response<LogoutResponse>

    @POST(REFRESH_TOKEN)
    suspend fun refreshToken(
        @Body refreshToken: RefreshTokenRequestBody
    ): Response<UserPreferences>
}