package com.soundhub.data.repository.implementations

import android.content.Context
import android.util.Log
import com.google.gson.Gson
import com.soundhub.BuildConfig
import com.soundhub.data.api.responses.HttpResult
import com.soundhub.data.api.responses.lastfm.LastFmSessionResponse
import com.soundhub.data.api.responses.lastfm.LastFmUserInfoResponse
import com.soundhub.data.api.services.LastFmService
import com.soundhub.data.repository.BaseRepository
import com.soundhub.data.repository.LastFmRepository
import com.soundhub.utils.extensions.string.md5
import javax.inject.Inject

class LastFmRepositoryImpl @Inject constructor(
	private val lastFmService: LastFmService,
	gson: Gson,
	context: Context
) : LastFmRepository, BaseRepository(gson, context) {

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
			Log.d("LastFmRepository", "getUserInfo[1]: $response")
			return handleResponse(response)
		} catch (e: Exception) {
			return handleException(e)
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