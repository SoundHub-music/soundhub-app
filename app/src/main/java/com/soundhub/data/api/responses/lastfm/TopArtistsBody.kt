package com.soundhub.data.api.responses.lastfm

import com.google.gson.annotations.SerializedName

data class TopArtistsBody(
	@SerializedName("artist")
	val artist: List<LastFmArtist>,

	@SerializedName("@attr")
	val pagination: LastFmPaginationResponse?
)

data class LastFmPaginationResponse(
	val tag: String? = null,
	val page: String = "1",
	val perPage: String = "1",
	val totalPages: String = "1",
	val total: String = "1",
)