package com.soundhub.data.api.responses.lastfm

import com.google.gson.annotations.SerializedName

data class LastFmSimilarArtistList(
	@SerializedName("artist") val artists: List<LastFmSimilarArtist>
)

data class LastFmSimilarArtist(
	@SerializedName("name") val name: String,
	@SerializedName("url") val url: String,
	@SerializedName("image") val images: List<LastFmImage>
)