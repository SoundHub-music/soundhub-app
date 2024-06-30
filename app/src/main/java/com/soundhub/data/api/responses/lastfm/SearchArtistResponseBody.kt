package com.soundhub.data.api.responses.lastfm

import com.google.gson.annotations.SerializedName

data class SearchArtistResponseBody(
    @SerializedName("results")
    val results: SearchResults
)

data class SearchResults(
    @SerializedName("artistmatches")
    val artistMatches: TopArtistsBody
)