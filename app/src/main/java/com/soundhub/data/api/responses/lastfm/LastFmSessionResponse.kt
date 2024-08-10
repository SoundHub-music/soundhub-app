package com.soundhub.data.api.responses.lastfm

data class LastFmSessionResponse(
	val message: String?,
	val error: Int?,
	val session: LastFmSessionBody
)

data class LastFmSessionBody(
	val name: String,
	val key: String,
	val subscriber: Int
)
