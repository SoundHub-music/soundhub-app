package com.soundhub.data.states

import com.soundhub.data.api.responses.discogs.DiscogsResponsePagination
import com.soundhub.data.enums.ApiStatus
import com.soundhub.data.model.Artist

data class ArtistUiState(
	val status: ApiStatus = ApiStatus.LOADING,
	val artists: List<Artist> = emptyList(),
//    val currentPage: Int = 1,
	val chosenArtists: List<Artist> = emptyList(),
	val pagination: DiscogsResponsePagination? = null
)