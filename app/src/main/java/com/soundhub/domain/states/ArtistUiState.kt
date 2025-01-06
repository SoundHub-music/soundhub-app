package com.soundhub.domain.states

import com.soundhub.domain.model.Artist

data class ArtistUiState(
	val chosenArtists: List<Artist> = emptyList(),
)