package com.soundhub.data.api

import com.soundhub.data.model.Post
import com.soundhub.utils.ApiEndpoints
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
        @Path(ApiEndpoints.Posts.POST_ID_DYNAMIC_PARAM) id: UUID,
        @Header("Authorization") accessToken: String?
    ): Response<Post?>

    @GET(ApiEndpoints.Posts.GET_POSTS_BY_AUTHOR_ID)
    suspend fun getPostsByAuthorId(
        @Path(ApiEndpoints.Posts.AUTHOR_ID_DYNAMIC_PARAM)
        authorId: UUID,
        @Header("Authorization") accessToken: String?
    ): Response<List<Post>>

    @POST(ApiEndpoints.Posts.ADD_POST)
    @Multipart
    suspend fun addPost(
        @Header("Authorization") accessToken: String?,
        @Part("postDto") post: RequestBody,
        @Part images: List<MultipartBody.Part?> = emptyList()
    ): Response<Post>

    @PUT(ApiEndpoints.Posts.TOGGLE_LIKE)
    suspend fun toggleLike(
        @Header("Authorization") accessToken: String?,
        @Path(ApiEndpoints.Posts.POST_ID_DYNAMIC_PARAM) postId: UUID
    ): Response<Post>

    @DELETE(ApiEndpoints.Posts.DELETE)
    suspend fun deletePost(
        @Header("Authorization") accessToken: String?,
        @Path(ApiEndpoints.Posts.POST_ID_DYNAMIC_PARAM) postId: UUID
    ): Response<RequestBody>

    @PUT(ApiEndpoints.Posts.UPDATE)
    suspend fun updatePost(
        @Header("Authorization") accessToken: String?,
        @Path(ApiEndpoints.Posts.POST_ID_DYNAMIC_PARAM) postId: UUID,
        @Part("postDto") post: RequestBody,
        @Part images: List<MultipartBody.Part?> = emptyList(),
        @Part deletedImages: List<MultipartBody.Part?> = emptyList()
    ): Response<Post>
}