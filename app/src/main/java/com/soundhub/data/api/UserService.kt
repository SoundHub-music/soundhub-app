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
import java.util.UUID

interface UserService {
    @GET(ApiEndpoints.SoundHub.CURRENT_USER)
    suspend fun getCurrentUser(
        @Header(HttpUtils.AUTHORIZATION_HEADER) accessToken: String?
    ): Response<User?>

    @GET(ApiEndpoints.SoundHub.GET_USER_BY_ID)
    suspend fun getUserById(
        @Path(ApiEndpoints.SoundHub.USER_ID_DYNAMIC_PARAM) id: UUID?,
        @Header(HttpUtils.AUTHORIZATION_HEADER) accessToken: String?
    ): Response<User?>

    @PUT(ApiEndpoints.SoundHub.UPDATE_USER)
    @Multipart
    suspend fun updateUserById(
        @Path(ApiEndpoints.SoundHub.USER_ID_DYNAMIC_PARAM)
        id: UUID?,
        @Part("userDto") user: RequestBody,
        @Part userAvatar: MultipartBody.Part?,
        @Header(HttpUtils.AUTHORIZATION_HEADER) accessToken: String?
    ): Response<User>

    @PUT(ApiEndpoints.SoundHub.ADD_FRIEND)
    suspend fun addFriend(
        @Header(HttpUtils.AUTHORIZATION_HEADER) accessToken: String?,
        @Path(ApiEndpoints.SoundHub.FRIEND_ID_DYNAMIC_PARAM)
        friendId: UUID
    ): Response<User>

    @PUT(ApiEndpoints.SoundHub.DELETE_FRIEND)
    suspend fun deleteFriend(
        @Header(HttpUtils.AUTHORIZATION_HEADER) accessToken: String?,
        @Path(ApiEndpoints.SoundHub.FRIEND_ID_DYNAMIC_PARAM)
        friendId: UUID
    ): Response<User>

    @GET(ApiEndpoints.SoundHub.GET_RECOMMENDED_FRIENDS)
    suspend fun getRecommendedFriends(
        @Header(HttpUtils.AUTHORIZATION_HEADER) accessToken: String?,
        @Path(ApiEndpoints.SoundHub.USER_ID_DYNAMIC_PARAM)
        userId: UUID?
    ): Response<List<User>>
}