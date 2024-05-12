package com.soundhub.data.api

import com.soundhub.data.model.Post
import com.soundhub.utils.ApiEndpoints
import com.soundhub.utils.HttpUtils
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Path
import java.util.UUID

interface PostService {
    @GET(ApiEndpoints.Posts.GET_POST_BY_ID)
    suspend fun getPostById(
        @Path(ApiEndpoints.Posts.POST_ID_DYNAMIC_PARAM)
        id: UUID,
        @Header(HttpUtils.AUTHORIZATION_HEADER)
        accessToken: String?
    ): Response<Post?>

    @GET(ApiEndpoints.Posts.GET_POSTS_BY_AUTHOR_ID)
    suspend fun getPostsByAuthorId(
        @Path(ApiEndpoints.Posts.AUTHOR_ID_DYNAMIC_PARAM)
        authorId: UUID,
        @Header(HttpUtils.AUTHORIZATION_HEADER)
        accessToken: String?
    ): Response<List<Post>>

    @POST(ApiEndpoints.Posts.ADD_POST)
    @Multipart
    suspend fun addPost(
        @Header(HttpUtils.AUTHORIZATION_HEADER)
        accessToken: String?,
        @Part("postDto") post: RequestBody,
        @Part files: List<MultipartBody.Part>
    ): Response<Post>

    @PUT(ApiEndpoints.Posts.TOGGLE_LIKE)
    suspend fun toggleLike(
        @Header(HttpUtils.AUTHORIZATION_HEADER)
        accessToken: String?,
        @Path(ApiEndpoints.Posts.POST_ID_DYNAMIC_PARAM)
        postId: UUID
    ): Response<Post>

    @DELETE(ApiEndpoints.Posts.DELETE)
    suspend fun deletePost(
        @Header(HttpUtils.AUTHORIZATION_HEADER)
        accessToken: String?,
        @Path(ApiEndpoints.Posts.POST_ID_DYNAMIC_PARAM)
        postId: UUID
    ): Response<UUID>

    @PUT(ApiEndpoints.Posts.UPDATE)
    @Multipart
    suspend fun updatePost(
        @Header(HttpUtils.AUTHORIZATION_HEADER)
        accessToken: String?,
        @Path(ApiEndpoints.Posts.POST_ID_DYNAMIC_PARAM)
        postId: UUID,
        @Part(value = "postDto") post: RequestBody,
        @Part images: List<MultipartBody.Part?> = emptyList(),
        @Part(value = "deleteFiles") deleteFiles: RequestBody? = null
    ): Response<Post>
}