package com.soundhub.data.repository.implementations

import android.util.Log
import com.soundhub.data.datastore.UserPreferences
import com.soundhub.data.model.ApiResult
import com.soundhub.data.api.AuthApi
import com.soundhub.data.api.LogoutRequestBody
import com.soundhub.data.api.LogoutResponse
import com.soundhub.data.api.RefreshTokenRequestBody
import com.soundhub.data.api.RegisterRequestBody
import com.soundhub.data.api.SignInRequestBody
import com.soundhub.data.repository.AuthRepository
import retrofit2.Response
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(private val authApi: AuthApi): AuthRepository {
    override suspend fun signIn(body: SignInRequestBody): ApiResult<UserPreferences?> {
        try {
            val signInResponse: Response<UserPreferences> = authApi.signIn(body)
            Log.d("sign_in_response", signInResponse.toString())

            if (!signInResponse.isSuccessful) {
                return ApiResult.Error(
                    message = signInResponse.message(),
                    code = signInResponse.code()
                )
            }

            return ApiResult.Success(data = signInResponse.body())
        }
        catch (e: Exception) {
            Log.e("AuthRepository[signIn]", e.message.toString())
            return ApiResult.Error(message = e.message)
        }

    }

    override suspend fun signUp(body: RegisterRequestBody): ApiResult<UserPreferences?> {
        try {
            val signUpResponse = authApi.signUp(body)
            if (!signUpResponse.isSuccessful) {
                return ApiResult.Error(
                    message = signUpResponse.message(),
                    code = signUpResponse.code()
                )
            }

            return ApiResult.Success(data = signUpResponse.body())
        }
        catch (e: Exception) {
            Log.e("AuthRepository[signUp]", e.message.toString())
            return ApiResult.Error(
                message = e.message
            )
        }
    }

    override suspend fun logout(body: LogoutRequestBody): ApiResult<LogoutResponse> {
        try {
            val logoutResponse: Response<LogoutResponse> = authApi.logout(body)
            if (!logoutResponse.isSuccessful) {
                return ApiResult.Error(
                    message = logoutResponse.message(),
                    code = logoutResponse.code()
                )
            }
            return ApiResult.Success(data = logoutResponse.body())
        }
        catch (e: Exception) {
            Log.e("AuthRepository[logout]", e.message.toString())
            return ApiResult.Error(message = e.message)
        }
    }

    override suspend fun refreshToken(body: RefreshTokenRequestBody): ApiResult<UserPreferences?> {
        try {
            val refreshTokenResponse = authApi.refreshToken(body)
            if (!refreshTokenResponse.isSuccessful)
                return ApiResult.Error(
                    message = refreshTokenResponse.message(),
                    code = refreshTokenResponse.code()
                )

            return ApiResult.Success(data = refreshTokenResponse.body())
        }
        catch (e: Exception) {
            Log.e("AuthRepository[refreshToken]", e.message.toString())
            return ApiResult.Error(message = e.message)
        }
    }

}