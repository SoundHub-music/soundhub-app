package com.soundhub.data.api.responses.lastfm

import com.google.gson.annotations.SerializedName

data class LastFmArtistTag(
	val name: String,
	val url: String
)

data class LastFmTags(
	@SerializedName("tag") val tags: List<LastFmArtistTag>
)