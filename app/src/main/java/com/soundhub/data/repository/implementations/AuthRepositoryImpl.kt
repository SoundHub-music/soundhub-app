package com.soundhub.data.repository.implementations

import android.content.Context
import android.util.Log
import com.google.gson.GsonBuilder
import com.soundhub.R
import com.soundhub.data.api.AuthService
import com.soundhub.data.api.requests.RefreshTokenRequestBody
import com.soundhub.data.api.requests.RegisterRequestBody
import com.soundhub.data.api.requests.SignInRequestBody
import com.soundhub.data.api.responses.ErrorResponse
import com.soundhub.data.api.responses.HttpResult
import com.soundhub.data.api.responses.LogoutResponse
import com.soundhub.data.datastore.UserPreferences
import com.soundhub.data.repository.AuthRepository
import com.soundhub.utils.HttpFileUtils
import com.soundhub.utils.Constants
import com.soundhub.utils.ContentTypes
import com.soundhub.utils.converters.LocalDateAdapter
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Response
import java.time.LocalDate
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val authService: AuthService,
    private val context: Context
): AuthRepository {
    private val gson = GsonBuilder()
        .registerTypeAdapter(LocalDate::class.java, LocalDateAdapter())
        .create()

    override suspend fun signIn(body: SignInRequestBody): HttpResult<UserPreferences?> {
        try {
            val signInResponse: Response<UserPreferences> = authService.signIn(body)
            Log.d("AuthRepository", "signIn[1]: $signInResponse")

            if (!signInResponse.isSuccessful) {
                val errorBody: ErrorResponse = gson
                    .fromJson(signInResponse.errorBody()?.charStream(), Constants.ERROR_BODY_TYPE)
                    ?: ErrorResponse(
                        status = signInResponse.code(),
                        detail = context.getString(R.string.toast_authorization_error)
                    )

                Log.d("AuthRepository", "signIn[2]: $errorBody")
                return HttpResult.Error(errorBody = errorBody)
            }

            return HttpResult.Success(body = signInResponse.body())
        }
        catch (e: Exception) {
            Log.e("AuthRepository", "signIn[3]: ${e.stackTraceToString()}")
            return HttpResult.Error(
                errorBody = ErrorResponse(detail = e.localizedMessage),
                throwable = e
            )
        }

    }

    override suspend fun signUp(body: RegisterRequestBody): HttpResult<UserPreferences?> {
        try {
            val avatarFormData: MultipartBody.Part? = HttpFileUtils.prepareMediaFormData(body.avatarUrl, context)
            val requestBody: RequestBody = gson.toJson(body)
                .toRequestBody(ContentTypes.JSON.type.toMediaTypeOrNull())

            val signUpResponse = authService.signUp(requestBody, avatarFormData)
            Log.d("AuthRepository", "signUp[1]: $signUpResponse")

            if (!signUpResponse.isSuccessful) {
                val errorBody: ErrorResponse = gson
                    .fromJson(signUpResponse.errorBody()?.charStream(), Constants.ERROR_BODY_TYPE)
                    ?: ErrorResponse(
                        detail = context.getString(R.string.toast_register_error),
                        status = signUpResponse.code()
                    )

                Log.d("AuthRepository", "signUp[2]: $errorBody")
                return HttpResult.Error(errorBody = errorBody)
            }

            return HttpResult.Success(body = signUpResponse.body())
        }
        catch (e: Exception) {
            Log.e("AuthRepository", "signUp[3]: ${e.stackTraceToString()}")
            return HttpResult.Error(
                errorBody = ErrorResponse(e.localizedMessage),
                throwable = e
            )
        }
    }

    override suspend fun logout(token: String?): HttpResult<LogoutResponse> {
        try {
            val logoutResponse: Response<LogoutResponse> = authService.logout("Bearer $token")
            Log.d("AuthRepository", "logout[1]: $logoutResponse")

            if (!logoutResponse.isSuccessful) {
                val errorBody: ErrorResponse = gson
                    .fromJson(logoutResponse.errorBody()?.charStream(), Constants.ERROR_BODY_TYPE)
                    ?: ErrorResponse(
                        status = logoutResponse.code(),
                        detail = context.getString(R.string.toast_logout_error)
                    )

                Log.d("AuthRepository", "logout[2]: $errorBody")
                return HttpResult.Error(errorBody = errorBody)
            }
            return HttpResult.Success(body = logoutResponse.body())
        }
        catch (e: Exception) {
            Log.e("AuthRepository", "logout[3]: ${e.stackTraceToString()}")
            return HttpResult.Error(
                errorBody = ErrorResponse(detail = e.localizedMessage),
                throwable = e
            )
        }
    }

    override suspend fun refreshToken(body: RefreshTokenRequestBody): HttpResult<UserPreferences?> {
        try {
            val refreshTokenResponse: Response<UserPreferences> = authService.refreshToken(body)
            Log.d("AuthRepository", "refreshToken[1]: ${refreshTokenResponse.raw()}")

            if (!refreshTokenResponse.isSuccessful) {
                val errorBody: ErrorResponse = gson
                    .fromJson(refreshTokenResponse.errorBody()?.charStream(), Constants.ERROR_BODY_TYPE)
                    ?: ErrorResponse(
                        status = refreshTokenResponse.code(),
                        detail = context.getString(R.string.toast_fetch_user_data_error)
                    )

                Log.d("AuthRepository", "refreshToken[2]: $errorBody")
                return HttpResult.Error(errorBody = errorBody)
            }

            return HttpResult.Success(body = refreshTokenResponse.body())
        }
        catch (e: Exception) {
            Log.e("AuthRepository", "refreshToken[3]: ${e.stackTraceToString()}")
            return HttpResult.Error(
                errorBody = ErrorResponse(detail = e.localizedMessage),
                throwable = e
            )
        }
    }
}