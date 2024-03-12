package com.soundhub.ui.states

data class UiState(
    var isSearchBarActive: Boolean = false,
    var searchBarText: String = "",
    var galleryImageUrls: List<String> = emptyList(),
)
