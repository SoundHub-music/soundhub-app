package com.soundhub.data.api.responses.lastfm

import com.google.gson.annotations.SerializedName

data class SearchArtistResponseBody(
	@SerializedName("results")
	val results: SearchResults
)

data class SearchResults(
	@SerializedName("artistmatches")
	val artistMatches: TopArtistsBody,

	@SerializedName("opensearch:totalResults")
	val totalItems: String,

	@SerializedName("opensearch:startIndex")
	val startIndex: String,

	@SerializedName("opensearch:itemsPerPage")
	val itemsPerPage: String,

	@SerializedName("opensearch:Query")
	val query: LastFmSearchQuery
)

data class LastFmSearchQuery(
	val role: String,
	val searchTerms: String,
	val startPage: String
)