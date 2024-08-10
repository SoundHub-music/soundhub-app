package com.soundhub.data.api.responses.discogs.release

import com.google.gson.annotations.SerializedName

data class DiscogsReleaseResponse(
	val id: Int,
	val status: String,
	val year: Int,

	@SerializedName("resource_url")
	val resourceUrl: String,
	val uri: String,

	@SerializedName("artists")
	val artists: List<DiscogsResponseArtistBody>,

	@SerializedName("master_id")
	val masterId: Int,

	@SerializedName("master_url")
	val masterUrl: String,
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
