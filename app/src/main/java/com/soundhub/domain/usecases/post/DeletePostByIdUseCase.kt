package com.soundhub.domain.usecases.post

import com.soundhub.data.repository.PostRepository
import java.util.UUID
import javax.inject.Inject

class DeletePostByIdUseCase @Inject constructor(
	private val postRepository: PostRepository,
) {
	suspend operator fun invoke(postId: UUID): Result<UUID?> = runCatching {

		var errorResponse: Throwable? = null
		var deletedPostId: UUID? = null

		postRepository.deletePost(postId)
			.onSuccess { response -> deletedPostId = response.body }
			.onFailure { error -> errorResponse = error.throwable }

		errorResponse?.let { throw Exception(errorResponse) }
		return@runCatching deletedPostId
	}
}