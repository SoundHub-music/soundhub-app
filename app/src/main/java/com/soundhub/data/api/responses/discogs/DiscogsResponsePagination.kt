package com.soundhub.data.api.responses.discogs

import com.google.gson.annotations.SerializedName

data class DiscogsResponsePagination(
	val page: Int = 0,
	val pages: Int = 0,

	@SerializedName("per_page")
	val perPage: Int = 0,
	val items: Int = 0,
	val urls: DiscogsUrlsPagination
)