package com.soundhub.data.api.responses.discogs

import com.google.gson.annotations.SerializedName

data class DiscogsEntityResponse(
    val id: Int,
    val title: String,
    val type: String,
    val thumb: String,

    @SerializedName("cover_image")
    val coverImage: String,

    @SerializedName("resource_url")
    val resourceUrl: String,

    @SerializedName("master_id")
    val masterId: String? = null,

    @SerializedName("master_url")
    val masterUrl: String? = null,
    val uri: String,

    val country: String? = null,
    val year: String? = null,
    val genre: List<String>? = null,
    val style: List<String>? = null,
)