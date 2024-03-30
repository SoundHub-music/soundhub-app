package com.soundhub.ui.authentication.postregistration.states

import com.soundhub.data.enums.ApiStatus
import com.soundhub.data.model.Artist

data class ArtistUiState(
    val status: ApiStatus = ApiStatus.LOADING,
    val artists: List<Artist> = emptyList(),
    val chosenArtists: List<Artist> = emptyList()
)