package com.soundhub.domain.usecases.post

import com.soundhub.data.api.responses.internal.ErrorResponse
import com.soundhub.data.local_database.dao.PostDao
import com.soundhub.domain.model.Post
import com.soundhub.domain.model.User
import com.soundhub.domain.repository.PostRepository
import com.soundhub.domain.states.ProfileUiState
import javax.inject.Inject

class GetPostsByUserUseCase @Inject constructor(
	private val postRepository: PostRepository,
	private val postDao: PostDao
) {
	suspend operator fun invoke(user: User, uiState: ProfileUiState): Result<List<Post>> =
		runCatching {
			val cache: List<Post> = postDao.getWallPosts()

			val isStateEmpty = uiState.userPosts.isEmpty()
			val stateHashCode = uiState.userPosts.hashCode()

			val shouldFetchData = cache.hashCode() != stateHashCode || isStateEmpty

			if (!shouldFetchData) {
				return@runCatching cache
			}

			val posts: MutableList<Post> = mutableListOf()
			var errorResponse: ErrorResponse? = null
			var throwable: Throwable? = null

			postRepository.getPostsByAuthorId(
				authorId = user.id
			).onSuccess { response ->
				postDao.updateWallPosts(response.body.orEmpty())

				posts.addAll(
					response.body
						?.sortedByDescending { p -> p.createdAt }
						.orEmpty()
				)

			}
				.onFailure { error ->
					errorResponse = error.errorBody
					throwable = error.throwable
				}

			if (errorResponse != null || throwable != null) {
				throw Exception(errorResponse?.detail, throwable)
			}

			return@runCatching posts
		}
}