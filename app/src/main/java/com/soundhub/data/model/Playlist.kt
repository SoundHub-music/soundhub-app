package com.soundhub.data.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable
import java.util.UUID

data class Playlist(
    @SerializedName("id")
    val id: UUID = UUID.randomUUID(),
    val name: String = "",

    @SerializedName("author")
    val author: User? = null,

    @SerializedName("tracks")
    val tracks: List<Track> = emptyList()
): Serializable
