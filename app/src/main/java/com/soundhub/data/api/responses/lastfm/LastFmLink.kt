package com.soundhub.data.api.responses.lastfm

import com.google.gson.annotations.SerializedName

data class Links(
	@SerializedName("link") val link: Link
)

data class Link(
	@SerializedName("#text") val text: String,
	@SerializedName("rel") val rel: String,
	@SerializedName("href") val href: String
)