package com.soundhub.data.repository

import com.soundhub.data.datastore.UserPreferences
import com.soundhub.data.model.ApiResult
import com.soundhub.data.api.LogoutRequestBody
import com.soundhub.data.api.LogoutResponse
import com.soundhub.data.api.RefreshTokenRequestBody
import com.soundhub.data.api.RegisterRequestBody
import com.soundhub.data.api.SignInRequestBody

interface AuthRepository {
    suspend fun signIn(body: SignInRequestBody): ApiResult<UserPreferences?>
    suspend fun signUp(body: RegisterRequestBody): ApiResult<UserPreferences?>
    suspend fun logout(body: LogoutRequestBody): ApiResult<LogoutResponse>
    suspend fun refreshToken(body: RefreshTokenRequestBody): ApiResult<UserPreferences?>
}