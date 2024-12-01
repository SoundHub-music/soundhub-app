package com.soundhub.domain.repository

import com.soundhub.data.api.responses.internal.HttpResult
import com.soundhub.data.api.responses.lastfm.LastFmSessionResponse
import com.soundhub.data.api.responses.lastfm.LastFmUserInfoResponse

interface LastFmRepository {
	suspend fun getMobileSession(
		username: String,
		password: String
	): HttpResult<LastFmSessionResponse>

	suspend fun getUserInfo(username: String): HttpResult<LastFmUserInfoResponse>
}