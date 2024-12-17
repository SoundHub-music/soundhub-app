package com.soundhub.domain.states

import com.soundhub.domain.model.User

data class UiState(
	val isSearchBarActive: Boolean = false,
	val searchBarText: String = "",
	val galleryImageUrls: List<String> = emptyList(),
	val currentRoute: String? = null,
	val authorizedUser: User? = null,
)
