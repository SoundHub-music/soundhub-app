package com.soundhub.data.repository

import com.soundhub.data.api.requests.RefreshTokenRequestBody
import com.soundhub.data.api.requests.RegisterRequestBody
import com.soundhub.data.api.requests.SignInRequestBody
import com.soundhub.data.datastore.UserPreferences
import com.soundhub.data.api.responses.HttpResult
import com.soundhub.data.api.responses.LogoutResponse

interface AuthRepository {
    suspend fun signIn(body: SignInRequestBody): HttpResult<UserPreferences?>
    suspend fun signUp(body: RegisterRequestBody): HttpResult<UserPreferences?>
    suspend fun logout(accessToken: String?): HttpResult<LogoutResponse>
    suspend fun refreshToken(body: RefreshTokenRequestBody): HttpResult<UserPreferences?>
}