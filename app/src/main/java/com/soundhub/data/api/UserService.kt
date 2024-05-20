package com.soundhub.data.api

import com.soundhub.data.model.User
import com.soundhub.utils.ApiEndpoints.Users.ADD_FRIEND
import com.soundhub.utils.ApiEndpoints.Users.CURRENT_USER
import com.soundhub.utils.ApiEndpoints.Users.DELETE_FRIEND
import com.soundhub.utils.ApiEndpoints.Users.FRIEND_ID_DYNAMIC_PARAM
import com.soundhub.utils.ApiEndpoints.Users.GET_FRIENDS
import com.soundhub.utils.ApiEndpoints.Users.GET_RECOMMENDED_FRIENDS
import com.soundhub.utils.ApiEndpoints.Users.GET_USER_BY_ID
import com.soundhub.utils.ApiEndpoints.Users.SEARCH_PARAM_NAME
import com.soundhub.utils.ApiEndpoints.Users.SEARCH_USER
import com.soundhub.utils.ApiEndpoints.Users.TOGGLE_ONLINE
import com.soundhub.utils.ApiEndpoints.Users.UPDATE_USER
import com.soundhub.utils.ApiEndpoints.Users.USER_ID_DYNAMIC_PARAM
import com.soundhub.utils.HttpUtils.Companion.AUTHORIZATION_HEADER
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
    @GET(CURRENT_USER)
    suspend fun getCurrentUser(
        @Header(AUTHORIZATION_HEADER)
        accessToken: String?
    ): Response<User?>

    @GET(GET_USER_BY_ID)
    suspend fun getUserById(
        @Path(USER_ID_DYNAMIC_PARAM)
        id: UUID?,
        @Header(AUTHORIZATION_HEADER)
        accessToken: String?
    ): Response<User?>

    @PUT(UPDATE_USER)
    @Multipart
    suspend fun updateUserById(
        @Header(AUTHORIZATION_HEADER)
        accessToken: String?,
        @Path(USER_ID_DYNAMIC_PARAM)
        id: UUID?,
        @Part("userDto") user: RequestBody,
        @Part userAvatar: MultipartBody.Part?
    ): Response<User>

    @PUT(ADD_FRIEND)
    suspend fun addFriend(
        @Header(AUTHORIZATION_HEADER)
        accessToken: String?,
        @Path(FRIEND_ID_DYNAMIC_PARAM)
        friendId: UUID
    ): Response<User>

    @PUT(DELETE_FRIEND)
    suspend fun deleteFriend(
        @Header(AUTHORIZATION_HEADER)
        accessToken: String?,
        @Path(FRIEND_ID_DYNAMIC_PARAM)
        friendId: UUID
    ): Response<User>

    @GET(GET_RECOMMENDED_FRIENDS)
    suspend fun getRecommendedFriends(
        @Header(AUTHORIZATION_HEADER)
        accessToken: String?
    ): Response<List<User>>

    @GET(GET_FRIENDS)
    suspend fun getFriendsByUserId(
        @Header(AUTHORIZATION_HEADER)
        accessToken: String?,
        @Path(USER_ID_DYNAMIC_PARAM)
        userId: UUID
    ): Response<List<User>>

    @GET(SEARCH_USER)
    suspend fun searchUserByFullName(
        @Header(AUTHORIZATION_HEADER)
        accessToken: String?,
        @Query(SEARCH_PARAM_NAME)
        name: String
    ): Response<List<User>>

    @PUT(TOGGLE_ONLINE)
    suspend fun toggleUserOnline(
        @Header(AUTHORIZATION_HEADER)
        accessToken: String?
    ): Response<User?>
}