package com.soundhub.ui.pages.postline

import com.soundhub.data.enums.ApiStatus
import com.soundhub.data.model.Post

data class PostLineUiState(
	val posts: List<Post> = emptyList(),
	val status: ApiStatus = ApiStatus.LOADING
)
