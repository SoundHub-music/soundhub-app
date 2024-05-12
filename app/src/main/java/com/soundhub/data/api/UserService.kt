package com.soundhub.data.api

import com.soundhub.data.model.User
import com.soundhub.utils.ApiEndpoints
import com.soundhub.utils.HttpUtils
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query
import java.util.UUID

interface UserService {
    @GET(ApiEndpoints.Users.CURRENT_USER)
    suspend fun getCurrentUser(
        @Header(HttpUtils.AUTHORIZATION_HEADER)
        accessToken: String?
    ): Response<User?>

    @GET(ApiEndpoints.Users.GET_USER_BY_ID)
    suspend fun getUserById(
        @Path(ApiEndpoints.Users.USER_ID_DYNAMIC_PARAM)
        id: UUID?,
        @Header(HttpUtils.AUTHORIZATION_HEADER)
        accessToken: String?
    ): Response<User?>

    @PUT(ApiEndpoints.Users.UPDATE_USER)
    @Multipart
    suspend fun updateUserById(
        @Header(HttpUtils.AUTHORIZATION_HEADER)
        accessToken: String?,
        @Path(ApiEndpoints.Users.USER_ID_DYNAMIC_PARAM)
        id: UUID?,
        @Part("userDto") user: RequestBody,
        @Part userAvatar: MultipartBody.Part?
    ): Response<User>

    @PUT(ApiEndpoints.Users.ADD_FRIEND)
    suspend fun addFriend(
        @Header(HttpUtils.AUTHORIZATION_HEADER)
        accessToken: String?,
        @Path(ApiEndpoints.Users.FRIEND_ID_DYNAMIC_PARAM)
        friendId: UUID
    ): Response<User>

    @PUT(ApiEndpoints.Users.DELETE_FRIEND)
    suspend fun deleteFriend(
        @Header(HttpUtils.AUTHORIZATION_HEADER)
        accessToken: String?,
        @Path(ApiEndpoints.Users.FRIEND_ID_DYNAMIC_PARAM)
        friendId: UUID
    ): Response<User>

    @GET(ApiEndpoints.Users.GET_RECOMMENDED_FRIENDS)
    suspend fun getRecommendedFriends(
        @Header(HttpUtils.AUTHORIZATION_HEADER)
        accessToken: String?,
        @Path(ApiEndpoints.Users.USER_ID_DYNAMIC_PARAM)
        userId: UUID?
    ): Response<List<User>>

    @GET(ApiEndpoints.Users.GET_FRIENDS)
    suspend fun getFriendsByUserId(
        @Header(HttpUtils.AUTHORIZATION_HEADER)
        accessToken: String?,
        @Path(ApiEndpoints.Users.USER_ID_DYNAMIC_PARAM)
        userId: UUID
    ): Response<List<User>>

    @GET(ApiEndpoints.Users.SEARCH_USER)
    suspend fun searchUserByFullName(
        @Header(HttpUtils.AUTHORIZATION_HEADER)
        accessToken: String?,
        @Query(ApiEndpoints.Users.SEARCH_PARAM_NAME)
        name: String
    ): Response<List<User>>
}