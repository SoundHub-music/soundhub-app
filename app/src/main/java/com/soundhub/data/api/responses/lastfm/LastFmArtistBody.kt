package com.soundhub.data.api.responses.lastfm

data class LastFmArtistBody(
	val name: String,
	val mbid: String,
	val url: String,
	val streamable: String
)