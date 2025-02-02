package com.soundhub.data.api.responses.lastfm

import com.google.gson.annotations.SerializedName

data class LastFmArtist(
	@SerializedName("name") val name: String,
	@SerializedName("mbid") val mbid: String,
	@SerializedName("url") val url: String,
	@SerializedName("image") val images: List<LastFmImage>,
	@SerializedName("streamable") val streamable: String,
	@SerializedName("ontour") val onTour: String?,
	@SerializedName("stats") val stats: LastFmStats?,
	@SerializedName("similar") val similar: LastFmSimilarArtistList?,
	@SerializedName("tags") val tags: LastFmTags?,
	@SerializedName("bio") val bio: LastFmBio?
)
