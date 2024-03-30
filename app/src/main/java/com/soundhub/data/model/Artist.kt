package com.soundhub.data.model

import com.google.gson.annotations.SerializedName

data class Artist(
    val id: Int = 0,
    val name: String,
    val description: String = "",
    var genres: List<String> = emptyList(),
    var styles: List<String> = emptyList(),

    @SerializedName("albums")
    val albums: List<Album> = emptyList(),
    val thumbnailUrl: String? = null
)