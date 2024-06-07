package com.soundhub.ui.states

import com.soundhub.data.model.User

data class UiState(
    val isSearchBarActive: Boolean = false,
    val searchBarText: String = "",
    val galleryImageUrls: List<String> = emptyList(),
    val currentRoute: String? = null,
    val authorizedUser: User? = null,
)
