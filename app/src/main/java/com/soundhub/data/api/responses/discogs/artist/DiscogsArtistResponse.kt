package com.soundhub.data.api.responses.discogs.artist

import com.google.gson.annotations.SerializedName
import com.soundhub.data.api.responses.discogs.release.DiscogsResponseImagesBody

data class DiscogsArtistResponse(
	val name: String,
	val id: Int,

	@SerializedName("resource_id")
	val resourceId: String,
	val uri: String,

	@SerializedName("releases_url")
	val releasesUrl: String,
	val images: List<DiscogsResponseImagesBody>,

	@SerializedName("realname")
	val realName: String,
	val urls: List<String>,
	val groups: List<DiscogsArtistGroupsResponse>
)