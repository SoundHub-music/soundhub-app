package com.soundhub.data.api.responses.lastfm

import com.google.gson.annotations.SerializedName

data class LastFmTopTagsAttribute(
    val offset: Int,

    @SerializedName("num_res")
    val numRes: Int,
    val total: Int
)