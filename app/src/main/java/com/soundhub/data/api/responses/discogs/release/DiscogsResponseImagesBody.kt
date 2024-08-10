package com.soundhub.data.api.responses.discogs.release

data class DiscogsResponseImagesBody(
	val type: String,
	val uri: String,
	val resource_url: String,
	val width: Int,
	val height: Int
)