package com.soundhub.ui.authentication.postregistration.states

import com.soundhub.data.enums.ApiStatus
import com.soundhub.data.model.Artist

data class ArtistUiState(
    val status: ApiStatus = ApiStatus.LOADING,
    val artists: List<Artist> = emptyList(),
    val currentPage: Int = 1,
    val chosenArtists: List<Artist> = emptyList()
)