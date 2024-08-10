package com.soundhub.data.api.responses.lastfm

import com.google.gson.annotations.SerializedName

data class LastFmGenreResponse(
	@SerializedName("toptags")
	val topTags: LastFmTopTagsBody,
)