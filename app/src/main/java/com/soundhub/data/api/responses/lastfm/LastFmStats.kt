package com.soundhub.data.api.responses.lastfm

import com.google.gson.annotations.SerializedName

data class LastFmStats(
	@SerializedName("listeners") val listeners: String,
	@SerializedName("playcount") val playCount: String
)

