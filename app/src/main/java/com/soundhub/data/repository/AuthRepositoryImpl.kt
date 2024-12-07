package com.soundhub.data.repository

import android.content.Context
import android.util.Log
import com.google.gson.Gson
import com.soundhub.data.api.requests.RefreshTokenRequestBody
import com.soundhub.data.api.requests.RegisterRequestBody
import com.soundhub.data.api.requests.SignInRequestBody
import com.soundhub.data.api.responses.internal.HttpResult
import com.soundhub.data.api.responses.internal.LogoutResponse
import com.soundhub.data.api.services.AuthService
import com.soundhub.data.datastore.model.UserPreferences
import com.soundhub.domain.model.User
import com.soundhub.domain.repository.AuthRepository
import com.soundhub.domain.repository.BaseRepository
import com.soundhub.domain.repository.UserRepository
import com.soundhub.utils.enums.ContentTypes
import com.soundhub.utils.lib.HttpUtils
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Response
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
	private val authService: AuthService,
	private val userRepository: UserRepository,
	private val context: Context,
	private val gson: Gson
) : AuthRepository, BaseRepository(gson, context) {
	override suspend fun signIn(body: SignInRequestBody): HttpResult<UserPreferences> {
		try {
			val signInResponse: Response<UserPreferences> = authService.signIn(body)
			Log.d("AuthRepository", "signIn[1]: $signInResponse")

			return handleResponse(signInResponse)
		} catch (e: Exception) {
			Log.e("AuthRepository", "signIn[2]: ${e.stackTraceToString()}")
			return handleException(e)
		}
	}

	override suspend fun signUp(body: RegisterRequestBody): HttpResult<UserPreferences> {
		try {
			val avatarFormData: MultipartBody.Part? = HttpUtils
				.prepareMediaFormData(body.avatarUrl, context)

			val requestBody: RequestBody = gson.toJson(body)
				.toRequestBody(ContentTypes.JSON.type.toMediaTypeOrNull())

			val signUpResponse = authService.signUp(requestBody, avatarFormData)
			Log.d("AuthRepository", "signUp[1]: $signUpResponse")

			return handleResponse(signUpResponse)
		} catch (e: Exception) {
			Log.e("AuthRepository", "signUp[2]: ${e.stackTraceToString()}")
			return handleException(e)
		}
	}

	override suspend fun logout(
		authorizedUser: User?,
		accessToken: String?
	): HttpResult<LogoutResponse> {
		try {
			val logoutResponse: Response<LogoutResponse> = authService.logout(
				HttpUtils.getBearerToken(accessToken)
			)
			Log.d("AuthRepository", "logout[1]: $logoutResponse")

			return handleResponse(logoutResponse, beforeReturningActions = {
				if (authorizedUser?.online == true)
					userRepository.updateUserOnline(false)
			})

		} catch (e: Exception) {
			Log.e("AuthRepository", "logout[3]: ${e.stackTraceToString()}")
			return handleException(e)
		}
	}

	override suspend fun refreshToken(body: RefreshTokenRequestBody): HttpResult<UserPreferences> {
		try {
			val response: Response<UserPreferences> = authService.refreshToken(body)
			Log.d("AuthRepository", "refreshToken[1]: $response")

			return handleResponse(response)
		} catch (e: Exception) {
			Log.e("AuthRepository", "refreshToken[3]: ${e.stackTraceToString()}")
			return handleException(e)
		}
	}
}