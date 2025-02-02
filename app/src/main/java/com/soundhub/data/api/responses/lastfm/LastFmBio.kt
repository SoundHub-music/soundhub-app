package com.soundhub.data.api.responses.lastfm

import com.google.gson.annotations.SerializedName

data class LastFmBio(
	@SerializedName("links") val links: Links,
	@SerializedName("published") val published: String,
	@SerializedName("summary") val summary: String,
	@SerializedName("content") val content: String
)

