package com.soundhub.data.api.responses.lastfm

import com.google.gson.annotations.SerializedName

data class LastFmArtistInfo(
	@SerializedName("artist") val artist: LastFmArtist
)
