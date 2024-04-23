package com.soundhub.data.api.responses.lastfm

import com.google.gson.annotations.SerializedName

data class TopArtistsBody(
    @SerializedName("artist")
    val artist: List<LastFmArtistBody>
)