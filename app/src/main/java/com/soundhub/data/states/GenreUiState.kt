package com.soundhub.data.states

import com.soundhub.data.enums.ApiStatus
import com.soundhub.data.model.Genre

data class GenreUiState(
	val status: ApiStatus = ApiStatus.LOADING,
	val genres: List<Genre> = emptyList(),
	val chosenGenres: List<Genre> = emptyList()
)