package com.soundhub.data.model

import com.google.gson.annotations.SerializedName
import java.util.UUID

data class Artist(
    @SerializedName("id")
    val id: UUID = UUID.randomUUID(),
    val name: String,
    val description: String = "",

    @SerializedName("albums")
    val albums: List<Album> = emptyList(),
    val thumbnailUrl: String? = null
)