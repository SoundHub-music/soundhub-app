package com.soundhub.data.repository

import android.content.Context
import android.util.Log
import com.google.gson.Gson
import com.soundhub.data.api.responses.internal.HttpResult
import com.soundhub.data.api.services.PostService
import com.soundhub.domain.model.Post
import com.soundhub.domain.repository.PostRepository
import com.soundhub.domain.repository.Repository
import com.soundhub.domain.usecases.user.LoadAllUserDataUseCase
import com.soundhub.utils.enums.ContentTypes
import com.soundhub.utils.lib.HttpUtils
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Response
import java.util.UUID
import javax.inject.Inject

class PostRepositoryImpl @Inject constructor(
	private val postService: PostService,
	private val context: Context,
	private val loadAllUserDataUseCase: LoadAllUserDataUseCase,
	private val gson: Gson
) : PostRepository, Repository(gson, context) {
	override suspend fun getPostById(postId: UUID): HttpResult<Post?> {
		try {
			val response: Response<Post?> = postService.getPostById(postId)
			Log.d("PostRepository", "getPostById[1]: $response")

			return handleResponse<Post?>(response) {
				val fetchedPost: Post? = response.body()
				fetchedPost?.author?.let { loadAllUserDataUseCase(it) }

				return@handleResponse HttpResult.Success(body = fetchedPost)
			}
		} catch (e: Exception) {
			Log.e("PostRepository", "getPostById[2]: ${e.stackTraceToString()}")
			return handleException(e)
		}
	}

	override suspend fun getPostsByAuthorId(authorId: UUID): HttpResult<List<Post>> {
		try {
			val response: Response<List<Post>> = postService.getPostsByAuthorId(authorId)
			Log.d("PostRepository", "getPostsByAuthorId[1]: $response")

			return handleResponse<List<Post>>(response) {
				val fetchedPosts: List<Post> = response.body().orEmpty()
				fetchedPosts.forEach { post ->
					post.author?.let { loadAllUserDataUseCase(it) }
				}

				return@handleResponse HttpResult.Success(body = fetchedPosts)
			}

		} catch (e: Exception) {
			Log.e("PostRepository", "getPostsByAuthorId[3]: ${e.stackTraceToString()}")
			return handleException(e)
		}
	}

	override suspend fun addPost(post: Post): HttpResult<Post> {
		try {
			val imagesFormDataList: List<MultipartBody.Part> = post.images.mapNotNull {
				HttpUtils.prepareMediaFormData(
					imageUrl = it,
					fileName = null,
					context = context
				)
			}

			val postRequestBody: RequestBody = gson.toJson(post)
				.toRequestBody(ContentTypes.JSON.type.toMediaTypeOrNull())

			val response: Response<Post> = postService.addPost(
				post = postRequestBody,
				files = imagesFormDataList
			)

			Log.d("PostRepository", "response: $response")

			return handleResponse(response)
		} catch (e: Exception) {
			Log.e("PostRepository", "addPost[3]: ${e.stackTraceToString()}")
			return handleException(e)
		}
	}

	override suspend fun toggleLike(postId: UUID): HttpResult<Post> {
		try {
			val response: Response<Post> = postService.toggleLike(postId)
			Log.d("PostRepository", "toggleLike[1]: $response")

			return handleResponse(response, beforeReturningActions = {
				val updatedPost = response.body()
				updatedPost?.author?.let { loadAllUserDataUseCase(it) }
			})
		} catch (e: Exception) {
			Log.e("PostRepository", "toggleLike[2]: ${e.stackTraceToString()}")
			return handleException(e)
		}
	}

	override suspend fun deletePost(postId: UUID): HttpResult<UUID> {
		try {
			val response: Response<UUID> = postService.deletePost(postId)
			Log.d("PostRepository", "deletePost[1]: $response")

			return handleResponse(response)
		} catch (e: Exception) {
			Log.e("PostRepository", "deletePost[3]: ${e.stackTraceToString()}")
			return handleException(e)
		}
	}

	override suspend fun updatePost(
		postId: UUID,
		post: Post,
		newImages: List<String>,
		imagesToBeDeleted: List<String>
	): HttpResult<Post> {
		try {
			val imageFormData: List<MultipartBody.Part?> = newImages.map {
				HttpUtils.prepareMediaFormData(
					imageUrl = it,
					fileName = post.id.toString(),
					context = context
				)
			}

			val postRequestBody: RequestBody = gson.toJson(post)
				.toRequestBody(ContentTypes.JSON.type.toMediaTypeOrNull())

			Log.d(
				"PostRepository",
				"updatePost[1]: json images to delete: ${gson.toJson(imagesToBeDeleted)}"
			)

			val imagesToBeDeletedRequestBody: RequestBody = gson.toJson(imagesToBeDeleted)
				.toRequestBody(ContentTypes.JSON.type.toMediaTypeOrNull())

			val response: Response<Post> = postService.updatePost(
				postId = postId,
				post = postRequestBody,
				images = imageFormData,
				deleteFiles = imagesToBeDeletedRequestBody
			)

			Log.d("PostRepository", "updatePost[2]: $response")

			return handleResponse(response, beforeReturningActions = {
				val updatedPost: Post? = response.body()
				updatedPost?.author?.let { loadAllUserDataUseCase(it) }
			})
		} catch (e: Exception) {
			Log.e("PostRepository", "updatePost[3]: ${e.stackTraceToString()}")
			return handleException(e)
		}
	}
}