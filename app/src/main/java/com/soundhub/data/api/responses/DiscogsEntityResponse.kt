package com.soundhub.data.api.responses

import com.google.gson.annotations.SerializedName

data class DiscogsResponse(
    @SerializedName("results")
    val results: List<DiscogsEntityResponse>,

    @SerializedName("pagination")
    val pagination: DiscogsResponsePagination
)

data class DiscogsResponsePagination(
    val page: Int = 0,
    val pages: Int = 0,
    val per_page: Int = 0,
    val items: Int = 0,
    val urls: DiscogsUrlsPagination
)

data class DiscogsUrlsPagination(
    val last: String,
    val next: String
)

data class DiscogsEntityResponse(
    val id: Int,
    val title: String,
    val type: String,
    val thumb: String,
    val cover_image: String,
    val resource_url: String,
    val master_id: String? = null,
    val master_url: String? = null,
    val uri: String,

    val country: String? = null,
    val year: String? = null,
    val genre: List<String>? = null,
    val style: List<String>? = null,

)