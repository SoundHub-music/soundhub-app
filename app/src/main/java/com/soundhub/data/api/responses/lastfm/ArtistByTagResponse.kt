package com.soundhub.data.api.responses.lastfm

import com.google.gson.annotations.SerializedName

data class ArtistsByTagResponse(
	@SerializedName("topartists")
	val topArtistsBody: TopArtistsBody
)