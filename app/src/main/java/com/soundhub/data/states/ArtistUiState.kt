package com.soundhub.data.states

import com.soundhub.data.api.responses.discogs.DiscogsResponsePagination
import com.soundhub.data.enums.ApiStatus
import com.soundhub.domain.model.Artist

data class ArtistUiState(
	val status: ApiStatus = ApiStatus.LOADING,
	val artists: List<Artist> = emptyList(),
	val chosenArtists: List<Artist> = emptyList(),
	val pagination: DiscogsResponsePagination? = null
)