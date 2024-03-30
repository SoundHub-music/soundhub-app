package com.soundhub.data.api.responses

data class DiscogsArtistResponse(
    val name: String,
    val id: Int,
    val resource_id: String,
    val uri: String,
    val releases_url: String,
    val images: List<DiscogsResponseImagesBody>,
    val realname: String,
    val urls: List<String>,
    val groups: List<DiscogsArtistGroupsResponse>
)



data class DiscogsArtistGroupsResponse(
    val id: Int,
    val name: String,
    val resource_url: String,
    val active: Boolean
)