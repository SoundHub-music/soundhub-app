package com.soundhub.domain.repository

import com.soundhub.data.api.responses.internal.HttpResult
import com.soundhub.data.api.responses.lastfm.LastFmArtistInfo
import com.soundhub.data.api.responses.lastfm.LastFmSessionResponse
import com.soundhub.data.api.responses.lastfm.LastFmUserInfoResponse
import java.util.UUID

interface LastFmRepository {
	suspend fun getMobileSession(
		username: String,
		password: String
	): HttpResult<LastFmSessionResponse>

	suspend fun getUserInfo(username: String): HttpResult<LastFmUserInfoResponse>

	suspend fun getArtistInfo(
		artist: String? = null,
		mbid: UUID?,
		lang: String? = null
	): HttpResult<LastFmArtistInfo>
}