package com.soundhub.data.repository

import com.soundhub.data.api.requests.RefreshTokenRequestBody
import com.soundhub.data.api.requests.RegisterRequestBody
import com.soundhub.data.api.requests.SignInRequestBody
import com.soundhub.data.api.responses.HttpResult
import com.soundhub.data.api.responses.LogoutResponse
import com.soundhub.data.datastore.model.UserPreferences
import com.soundhub.data.model.User

interface AuthRepository {
	suspend fun signIn(body: SignInRequestBody): HttpResult<UserPreferences>
	suspend fun signUp(body: RegisterRequestBody): HttpResult<UserPreferences>
	suspend fun logout(
		authorizedUser: User?,
		accessToken: String?
	): HttpResult<LogoutResponse>

	suspend fun refreshToken(body: RefreshTokenRequestBody): HttpResult<UserPreferences>
}