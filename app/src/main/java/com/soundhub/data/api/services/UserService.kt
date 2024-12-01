package com.soundhub.data.api.services

import com.soundhub.data.api.requests.CompatibleUsersRequestBody
import com.soundhub.data.api.responses.internal.CompatibleUsersResponse
import com.soundhub.data.api.responses.internal.UserExistenceResponse
import com.soundhub.data.model.User
import com.soundhub.utils.constants.ApiEndpoints.Users.ADD_FRIEND
import com.soundhub.utils.constants.ApiEndpoints.Users.CHECK_USER_EXISTENCE
import com.soundhub.utils.constants.ApiEndpoints.Users.COMPATIBLE_USERS
import com.soundhub.utils.constants.ApiEndpoints.Users.CURRENT_USER
import com.soundhub.utils.constants.ApiEndpoints.Users.DELETE_FRIEND
import com.soundhub.utils.constants.ApiEndpoints.Users.FRIEND_ID_DYNAMIC_PARAM
import com.soundhub.utils.constants.ApiEndpoints.Users.GET_FRIENDS
import com.soundhub.utils.constants.ApiEndpoints.Users.GET_RECOMMENDED_FRIENDS
import com.soundhub.utils.constants.ApiEndpoints.Users.GET_USER_BY_ID
import com.soundhub.utils.constants.ApiEndpoints.Users.SEARCH_PARAM_NAME
import com.soundhub.utils.constants.ApiEndpoints.Users.SEARCH_USER
import com.soundhub.utils.constants.ApiEndpoints.Users.TOGGLE_ONLINE
import com.soundhub.utils.constants.ApiEndpoints.Users.UPDATE_USER
import com.soundhub.utils.constants.ApiEndpoints.Users.USER_EMAIL_DYNAMIC_PARAM
import com.soundhub.utils.constants.ApiEndpoints.Users.USER_ID_DYNAMIC_PARAM
import com.soundhub.utils.constants.ApiEndpoints.Users.USER_REQUEST_BODY_NAME
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query
import java.util.UUID

interface UserService {
	@GET(CURRENT_USER)
	suspend fun getCurrentUser(): Response<User?>

	@GET(CHECK_USER_EXISTENCE)
	suspend fun checkUserExistenceByEmail(
		@Path(USER_EMAIL_DYNAMIC_PARAM)
		email: String
	): Response<UserExistenceResponse>

	@GET(GET_USER_BY_ID)
	suspend fun getUserById(
		@Path(USER_ID_DYNAMIC_PARAM)
		id: UUID?,
	): Response<User?>

	@PUT(UPDATE_USER)
	@Multipart
	suspend fun updateUserById(
		@Path(USER_ID_DYNAMIC_PARAM)
		id: UUID?,
		@Part(USER_REQUEST_BODY_NAME) user: RequestBody,
		@Part userAvatar: MultipartBody.Part?
	): Response<User>

	@PUT(ADD_FRIEND)
	suspend fun addFriend(
		@Path(FRIEND_ID_DYNAMIC_PARAM)
		friendId: UUID
	): Response<User>

	@PUT(DELETE_FRIEND)
	suspend fun deleteFriend(
		@Path(FRIEND_ID_DYNAMIC_PARAM)
		friendId: UUID
	): Response<User>

	@GET(GET_RECOMMENDED_FRIENDS)
	suspend fun getRecommendedFriends(): Response<List<User>>

	@POST(COMPATIBLE_USERS)
	suspend fun getUsersCompatibilityPercentage(
		@Body
		listUsersCompareWith: CompatibleUsersRequestBody
	): Response<CompatibleUsersResponse>

	@GET(GET_FRIENDS)
	suspend fun getFriendsByUserId(
		@Path(USER_ID_DYNAMIC_PARAM)
		userId: UUID
	): Response<List<User>>

	@GET(SEARCH_USER)
	suspend fun searchUserByFullName(
		@Query(SEARCH_PARAM_NAME)
		name: String
	): Response<List<User>>

	@PUT(TOGGLE_ONLINE)
	suspend fun toggleUserOnline(): Response<User?>
}