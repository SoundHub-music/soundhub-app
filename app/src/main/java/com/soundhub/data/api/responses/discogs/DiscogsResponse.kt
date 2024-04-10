package com.soundhub.data.api.responses.discogs

import com.google.gson.annotations.SerializedName

data class DiscogsResponse(
    @SerializedName("results")
    val results: List<DiscogsEntityResponse>,

    @SerializedName("pagination")
    val pagination: DiscogsResponsePagination
)