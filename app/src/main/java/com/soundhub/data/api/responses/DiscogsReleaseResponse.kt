package com.soundhub.data.api.responses

import com.google.gson.annotations.SerializedName

data class DiscogsReleaseResponse(
    val id: Int,
    val status: String,
    val year: Int,
    val resource_url: String,
    val uri: String,

    @SerializedName("artists")
    val artists: List<DiscogsResponseArtistBody>,
    val master_id: Int,
    val master_url: String,
    val title: String,
    val country: String,
    val released: String,

    @SerializedName("videos")
    val videos: List<DiscogsResponseVideoBody>,
    val genres: List<String>,
    val styles: List<String>,

    @SerializedName("tracklist")
    val tracklist: List<DiscogsResponseTracklistBody>,

    @SerializedName("images")
    val images: List<DiscogsResponseImagesBody>,
    val thumb: String
)


data class DiscogsResponseArtistBody(
    val name: String,
    val anv: String,
    val join: String,
    val role: String,
    val tracks: String,
    val id: Int,
    val resource_url: String,
)

data class DiscogsResponseVideoBody(
    val uri: String,
    val title: String,
    val description: String,
    val duration: Int
)

data class DiscogsResponseTracklistBody(
    val position: String,
    val type: String,
    val title: String,
    val duration: String
)

data class DiscogsResponseImagesBody(
    val type: String,
    val uri: String,
    val resource_url: String,
    val width: Int,
    val height: Int
)