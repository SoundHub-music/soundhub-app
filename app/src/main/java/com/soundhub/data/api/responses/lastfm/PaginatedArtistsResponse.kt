package com.soundhub.data.api.responses.lastfm

import com.soundhub.domain.model.Artist

data class PaginatedArtistsResponse(
	var artists: List<Artist> = emptyList(),
	var pagination: LastFmPaginationResponse? = null
)

