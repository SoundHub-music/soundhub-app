package com.soundhub.data.repository.implementations

import android.content.Context
import android.util.Log
import com.google.gson.GsonBuilder
import com.soundhub.R
import com.soundhub.data.api.PostService
import com.soundhub.data.api.responses.ErrorResponse
import com.soundhub.data.api.responses.HttpResult
import com.soundhub.data.model.Post
import com.soundhub.utils.HttpUtils
import com.soundhub.data.repository.PostRepository
import com.soundhub.domain.usecases.user.LoadAllUserDataUseCase
import com.soundhub.utils.constants.Constants
import com.soundhub.utils.enums.ContentTypes
import com.soundhub.utils.converters.json.LocalDateAdapter
import com.soundhub.utils.converters.json.LocalDateTimeAdapter
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Response
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.UUID
import javax.inject.Inject

class PostRepositoryImpl @Inject constructor(
    private val postService: PostService,
    private val context: Context,
    private val loadAllUserDataUseCase: LoadAllUserDataUseCase
): PostRepository {
    private val gson = GsonBuilder()
        .registerTypeAdapter(LocalDateTime::class.java, LocalDateTimeAdapter())
        .registerTypeAdapter(LocalDate::class.java, LocalDateAdapter())
        .create()

    override suspend fun getPostById(postId: UUID): HttpResult<Post?> {
        try {
            val response: Response<Post?> = postService.getPostById(postId)
            Log.d("PostRepository", "getPostById[1]: $response")

            if (!response.isSuccessful) {
                val errorBody: ErrorResponse = gson
                    .fromJson(response.errorBody()?.charStream(), Constants.ERROR_BODY_TYPE)
                    ?: ErrorResponse(
                        status = response.code(),
                        detail = response.message()
                    )

                Log.e("PostRepository", "getPostById[2]: $errorBody")
                return HttpResult.Error(errorBody = errorBody)
            }


            val fetchedPost: Post? = response.body()
            fetchedPost?.author?.let { loadAllUserDataUseCase(it) }

            return HttpResult.Success(body = fetchedPost)
        }
        catch (e: Exception) {
            Log.e("PostRepository", "getPostById[3]: ${e.stackTraceToString()}")
            return HttpResult.Error(
                errorBody = ErrorResponse(detail = e.localizedMessage),
                throwable = e
            )
        }
    }

    override suspend fun getPostsByAuthorId(authorId: UUID): HttpResult<List<Post>> {
        try {
            val response: Response<List<Post>> = postService.getPostsByAuthorId(authorId,)
            Log.d("PostRepository", "getPostsByAuthorId[1]: $response")

            if (!response.isSuccessful) {
                val errorBody: ErrorResponse = gson
                    .fromJson(response.errorBody()?.charStream(), Constants.ERROR_BODY_TYPE)
                    ?: ErrorResponse(
                        status = response.code(),
                        detail = response.message()
                    )
                Log.e("PostRepository", "getPostsByAuthorId[2]: $errorBody")
                return HttpResult.Error(errorBody = errorBody)
            }

            val fetchedPosts: List<Post> = response.body().orEmpty()
            fetchedPosts.forEach { post ->
                post.author?.let { loadAllUserDataUseCase(it) }
            }

            return HttpResult.Success(body = fetchedPosts)
        }
        catch (e: Exception) {
            Log.e("PostRepository", "getPostsByAuthorId[3]: ${e.stackTraceToString()}")
            return HttpResult.Error(
                errorBody = ErrorResponse(detail = e.localizedMessage),
                throwable = e
            )
        }
    }

    override suspend fun addPost(post: Post): HttpResult<Post> {
        try {
            val imagesFormDataList: List<MultipartBody.Part> = post.images.mapNotNull {
                HttpUtils.prepareMediaFormData(it, context)
            }

            val postRequestBody: RequestBody = gson.toJson(post)
                .toRequestBody(ContentTypes.JSON.type.toMediaTypeOrNull())

            val response: Response<Post> = postService.addPost(
                post = postRequestBody,
                files = imagesFormDataList
            )

            Log.d("PostRepository", "response: $response")

            if (!response.isSuccessful) {
                val errorBody: ErrorResponse = gson
                    .fromJson(response.errorBody()?.charStream(), Constants.ERROR_BODY_TYPE)
                    ?: ErrorResponse(
                        status = response.code(),
                        detail = context.getString(R.string.toast_update_error)
                    )

                Log.e("PostRepository", "addPost[2]: $errorBody")
                return HttpResult.Error(errorBody = errorBody)
            }

            return HttpResult.Success(body = response.body())
        }
        catch (e: Exception) {
            Log.e("PostRepository", "addPost[3]: ${e.stackTraceToString()}")
            return HttpResult.Error(
                errorBody = ErrorResponse(detail = e.localizedMessage),
                throwable = e
            )
        }
    }

    override suspend fun toggleLike(postId: UUID): HttpResult<Post> {
        try {
            val response: Response<Post> = postService.toggleLike(postId)
            Log.d("PostRepository", "toggleLike[1]: $response")

            if (!response.isSuccessful) {
                val errorBody: ErrorResponse = gson
                    .fromJson(response.errorBody()?.charStream(), Constants.ERROR_BODY_TYPE)
                Log.e("PostRepository", "toggleLike[2]: $errorBody")
                return HttpResult.Error(errorBody)
            }

            val updatedPost = response.body()
            updatedPost?.author?.let { loadAllUserDataUseCase(it) }

            return HttpResult.Success(body = response.body())
        }
        catch (e: Exception) {
            Log.e("PostRepository", "toggleLike[3]: ${e.stackTraceToString()}")
            return HttpResult.Error(
                errorBody = ErrorResponse(detail = e.localizedMessage),
                throwable = e
            )
        }
    }

    override suspend fun deletePost(postId: UUID): HttpResult<UUID> {
        try {
            val response: Response<UUID> = postService.deletePost(postId)
            Log.d("PostRepository", "deletePost[1]: $response")

            if (!response.isSuccessful) {
                val errorBody: ErrorResponse = gson
                    .fromJson(response.errorBody()?.charStream(), Constants.ERROR_BODY_TYPE)
                Log.e("PostRepository", "deletePost[2]: $errorBody")
                return HttpResult.Error(errorBody)
            }

            return HttpResult.Success(body = response.body())
        }
        catch (e: Exception) {
            Log.e("PostRepository", "deletePost[3]: ${e.stackTraceToString()}")
            return HttpResult.Error(
                errorBody = ErrorResponse(detail = e.localizedMessage),
                throwable = e
            )
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
                HttpUtils.prepareMediaFormData(it, context)
            }

            Log.d("PostRepository", "updatePost[1]: json -> ${gson.toJson(post)}")
            val postRequestBody: RequestBody = gson.toJson(post)
                .toRequestBody(ContentTypes.JSON.type.toMediaTypeOrNull())

            Log.d("PostRepository", "updatePost[2]: json images to delete: ${gson.toJson(imagesToBeDeleted)}")

            val imagesToBeDeletedRequestBody: RequestBody = gson.toJson(imagesToBeDeleted)
                .toRequestBody(ContentTypes.JSON.type.toMediaTypeOrNull())

            Log.d("PostRepository", "updatePost[3]: request body: $imagesToBeDeletedRequestBody")

            val response: Response<Post> = postService.updatePost(
                postId = postId,
                post = postRequestBody,
                images = imageFormData,
                deleteFiles = imagesToBeDeletedRequestBody
            )

            Log.d("PostRepository", "updatePost[4]: $response")

            if (!response.isSuccessful) {
                val errorBody: ErrorResponse = gson.fromJson(
                    response.errorBody()?.charStream(), Constants.ERROR_BODY_TYPE
                ) ?: ErrorResponse(status = response.code())

                Log.e("PostRepository", "updatePost[5]: $errorBody")
                return HttpResult.Error(errorBody = errorBody)
            }

            val updatedPost: Post? = response.body()
            updatedPost?.author?.let { loadAllUserDataUseCase(it) }

            return HttpResult.Success(body = response.body())
        }
        catch (e: Exception) {
            Log.e("PostRepository", "updatePost[6]: ${e.stackTraceToString()}")
            return HttpResult.Error(
                errorBody = ErrorResponse(detail = e.localizedMessage),
                throwable = e
            )
        }
    }
}