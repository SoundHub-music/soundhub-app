package com.soundhub.data.api.responses.lastfm

import com.google.gson.annotations.SerializedName

data class LastFmImage(
	@SerializedName("#text") val url: String,
	@SerializedName("size") val size: String
)
