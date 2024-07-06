package com.soundhub.data.api.services

import com.soundhub.data.model.Post
import com.soundhub.utils.ApiEndpoints.Posts.ADD_POST
import com.soundhub.utils.ApiEndpoints.Posts.AUTHOR_ID_DYNAMIC_PARAM
import com.soundhub.utils.ApiEndpoints.Posts.DELETE_POST
import com.soundhub.utils.ApiEndpoints.Posts.GET_POSTS_BY_AUTHOR_ID
import com.soundhub.utils.ApiEndpoints.Posts.GET_POST_BY_ID
import com.soundhub.utils.ApiEndpoints.Posts.POST_ID_DYNAMIC_PARAM
import com.soundhub.utils.ApiEndpoints.Posts.TOGGLE_LIKE
import com.soundhub.utils.ApiEndpoints.Posts.UPDATE_POST
import com.soundhub.utils.ApiEndpoints.Posts.IMAGES_TO_DELETE_NAME
import com.soundhub.utils.ApiEndpoints.Posts.POST_REQUEST_BODY_NAME
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Path
import java.util.UUID

interface PostService {
    @GET(GET_POST_BY_ID)
    suspend fun getPostById(
        @Path(POST_ID_DYNAMIC_PARAM)
        id: UUID,
    ): Response<Post?>

    @GET(GET_POSTS_BY_AUTHOR_ID)
    suspend fun getPostsByAuthorId(
        @Path(AUTHOR_ID_DYNAMIC_PARAM)
        authorId: UUID,
    ): Response<List<Post>>

    @POST(ADD_POST)
    @Multipart
    suspend fun addPost(
        @Part(POST_REQUEST_BODY_NAME) post: RequestBody,
        @Part files: List<MultipartBody.Part>
    ): Response<Post>

    @PUT(TOGGLE_LIKE)
    suspend fun toggleLike(
        @Path(POST_ID_DYNAMIC_PARAM)
        postId: UUID
    ): Response<Post>

    @DELETE(DELETE_POST)
    suspend fun deletePost(
        @Path(POST_ID_DYNAMIC_PARAM)
        postId: UUID
    ): Response<UUID>

    @PUT(UPDATE_POST)
    @Multipart
    suspend fun updatePost(
        @Path(POST_ID_DYNAMIC_PARAM)
        postId: UUID,
        @Part(POST_REQUEST_BODY_NAME) post: RequestBody,
        @Part images: List<MultipartBody.Part?> = emptyList(),
        @Part(IMAGES_TO_DELETE_NAME) deleteFiles: RequestBody? = null
    ): Response<Post>
}