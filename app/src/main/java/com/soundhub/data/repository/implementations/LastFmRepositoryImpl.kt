package com.soundhub.data.repository.implementations

import android.util.Log
import com.google.gson.Gson
import com.soundhub.BuildConfig
import com.soundhub.data.api.responses.ErrorResponse
import com.soundhub.data.api.responses.HttpResult
import com.soundhub.data.api.responses.lastfm.LastFmSessionResponse
import com.soundhub.data.api.responses.lastfm.LastFmUserInfoResponse
import com.soundhub.data.api.services.LastFmService
import com.soundhub.data.repository.LastFmRepository
import com.soundhub.utils.constants.Constants
import com.soundhub.utils.extensions.string.md5
import javax.inject.Inject

class LastFmRepositoryImpl @Inject constructor(
	private val lastFmService: LastFmService
) : LastFmRepository {
	override suspend fun getMobileSession(
		username: String,
		password: String
	): HttpResult<LastFmSessionResponse> {
		val sigKey: String = generateSigKey(username, password)
		val response = lastFmService.getMobileSession(
			username = username,
			password = password,
			apiSig = sigKey
		)

		return HttpResult.Success(body = response.body())
	}

	override suspend fun getUserInfo(username: String): HttpResult<LastFmUserInfoResponse> {
		try {
			val response = lastFmService.getUserInfo(username)
			Log.e("LastFmRepository", "getUserInfo[1]: $response")

			if (!response.isSuccessful) {
				val errorBody: ErrorResponse = Gson()
					.fromJson(response.errorBody()?.charStream(), Constants.ERROR_BODY_TYPE)
					?: ErrorResponse(status = response.code())

				Log.e("LastFmRepository", "getUserInfo[2]: $errorBody")
				return HttpResult.Error(errorBody = errorBody)
			}

			return HttpResult.Success(response.body())
		} catch (e: Exception) {
			return HttpResult.Error(
				throwable = e,
				errorBody = ErrorResponse(detail = e.localizedMessage)
			)
		}
	}

	private fun generateSigKey(
		username: String,
		password: String
	): String {
		val secret = BuildConfig.LAST_FM_SHARED_SECRET
		val apiKey = BuildConfig.LAST_FM_API_KEY

		val sigKey: String = StringBuilder()
			.append("api_key")
			.append(apiKey)
			.append("method")
			.append("auth.getMobileSession")
			.append("password")
			.append(password)
			.append("username")
			.append(username)
			.append(secret)
			.toString()
			.md5()

		return sigKey
	}
}