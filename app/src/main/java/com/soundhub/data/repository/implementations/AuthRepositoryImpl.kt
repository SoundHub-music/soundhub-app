package com.soundhub.data.repository.implementations

import android.content.Context
import android.util.Log
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.soundhub.data.api.AuthApi
import com.soundhub.data.api.requests.RefreshTokenRequestBody
import com.soundhub.data.api.requests.RegisterRequestBody
import com.soundhub.data.api.requests.SignInRequestBody
import com.soundhub.data.api.responses.ErrorResponse
import com.soundhub.data.api.responses.HttpResult
import com.soundhub.data.api.responses.LogoutResponse
import com.soundhub.data.datastore.UserPreferences
import com.soundhub.data.repository.AuthRepository
import com.soundhub.data.repository.FileRepositoryUtils
import com.soundhub.utils.Constants
import com.soundhub.utils.MediaTypes
import com.soundhub.utils.converters.LocalDateAdapter
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Response
import java.io.File
import java.time.LocalDate
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val authApi: AuthApi,
    private val context: Context
): AuthRepository, FileRepositoryUtils {
    override suspend fun signIn(body: SignInRequestBody): HttpResult<UserPreferences?> {
        try {
            val signInResponse: Response<UserPreferences> = authApi.signIn(body)
            Log.d("AuthRepository", "signIn[1]: $signInResponse")

            if (!signInResponse.isSuccessful) {
                val errorBody: ErrorResponse? = Gson()
                    .fromJson(signInResponse.errorBody()?.charStream(), Constants.ERROR_BODY_TYPE)
                Log.d("AuthRepository", "signIn[2]: ${errorBody.toString()}")
                return HttpResult.Error(errorBody = errorBody)
            }

            return HttpResult.Success(body = signInResponse.body())
        }
        catch (e: Exception) {
            Log.e("AuthRepository", "signIn[3]: ${e.stackTraceToString()}")
            return HttpResult.Error(errorBody = ErrorResponse(detail = e.message))
        }

    }

    override suspend fun signUp(body: RegisterRequestBody): HttpResult<UserPreferences?> {
        try {
            val gson = GsonBuilder()
                .registerTypeAdapter(LocalDate::class.java, LocalDateAdapter())
                .create()

            val tempFile: File? = createTempMediaFile(
                imageUri = body.avatarUrl,
                context = context
            )

            val avatarFormData = getImageFormData(tempFile, context)
            val requestBody = gson.toJson(body)
                .toRequestBody(MediaTypes.JSON.type.toMediaTypeOrNull())

            val signUpResponse = authApi.signUp(requestBody, avatarFormData)
            Log.d("AuthRepository", "signUp[1]: $signUpResponse")

            if (!signUpResponse.isSuccessful) {
                val errorBody: ErrorResponse? = Gson()
                    .fromJson(signUpResponse.errorBody()?.charStream(), Constants.ERROR_BODY_TYPE)
                Log.d("AuthRepository", "signUp[2]: ${errorBody.toString()}")
                return HttpResult.Error(errorBody = errorBody)
            }

            return HttpResult.Success(body = signUpResponse.body())
        }
        catch (e: Exception) {
            Log.e("AuthRepository", "signUp[3]: ${e.stackTraceToString()}")
            return HttpResult.Error(errorBody = ErrorResponse(e.message))
        }
    }

    override suspend fun logout(token: String?): HttpResult<LogoutResponse> {
        try {
            val logoutResponse: Response<LogoutResponse> = authApi.logout("Bearer $token")
            Log.d("AuthRepository", "logout[1]: $logoutResponse")

            if (!logoutResponse.isSuccessful) {
                val errorBody: ErrorResponse? = Gson()
                    .fromJson(logoutResponse.errorBody()?.charStream(), Constants.ERROR_BODY_TYPE)
                Log.d("AuthRepository", "logout[2]: ${errorBody.toString()}")
                return HttpResult.Error(errorBody = errorBody)
            }
            return HttpResult.Success(body = logoutResponse.body())
        }
        catch (e: Exception) {
            Log.e("AuthRepository", "logout[3]: ${e.stackTraceToString()}")
            return HttpResult.Error(errorBody = ErrorResponse(detail = e.message))
        }
    }

    override suspend fun refreshToken(body: RefreshTokenRequestBody): HttpResult<UserPreferences?> {
        try {
            val refreshTokenResponse: Response<UserPreferences> = authApi.refreshToken(body)
            Log.d("AuthRepository", "refreshToken[1]: ${refreshTokenResponse.raw()}")

            if (!refreshTokenResponse.isSuccessful) {
                val errorBody: ErrorResponse? = Gson()
                    .fromJson(refreshTokenResponse.errorBody()?.charStream(), Constants.ERROR_BODY_TYPE)
                Log.d("AuthRepository", "refreshToken[2]: ${errorBody.toString()}")
                return HttpResult.Error(errorBody = errorBody)
            }

            return HttpResult.Success(body = refreshTokenResponse.body())
        }
        catch (e: Exception) {
            Log.e("AuthRepository", "refreshToken[3]: ${e.stackTraceToString()}")
            return HttpResult.Error(errorBody = ErrorResponse(detail = e.message))
        }
    }
}