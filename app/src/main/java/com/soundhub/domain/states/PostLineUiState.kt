package com.soundhub.domain.states

import com.soundhub.data.enums.ApiStatus
import com.soundhub.domain.model.Post

data class PostLineUiState(
	val posts: List<Post> = emptyList(),
	val status: ApiStatus = ApiStatus.LOADING
)
