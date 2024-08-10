package com.soundhub.ui.viewmodels

import androidx.lifecycle.ViewModel
import com.soundhub.data.model.Post
import com.soundhub.data.model.User

class PostViewModel : ViewModel() {
	fun isPostLiked(user: User?, post: Post): Boolean = post.likes
		.map { it.id }.contains(user?.id)
}