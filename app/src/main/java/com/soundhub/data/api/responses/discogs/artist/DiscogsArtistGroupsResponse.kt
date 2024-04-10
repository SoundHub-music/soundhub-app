package com.soundhub.data.api.responses.discogs.artist

import com.google.gson.annotations.SerializedName

data class DiscogsArtistGroupsResponse(
    val id: Int,
    val name: String,

    @SerializedName("resource_url")
    val resourceUrl: String,
    val active: Boolean
)