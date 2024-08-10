package com.soundhub.data.api.responses.lastfm

import com.google.gson.annotations.SerializedName

data class LastFmTopTagsBody(
	@SerializedName("@attr")
	val attr: LastFmTopTagsAttribute,

	@SerializedName("tag")
	val tag: List<LastFmTag> = emptyList()
)