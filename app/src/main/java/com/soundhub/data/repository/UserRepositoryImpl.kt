package com.soundhub.data.repository

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.core.net.toUri
import com.google.gson.Gson
import com.soundhub.data.api.requests.CompatibleUsersRequestBody
import com.soundhub.data.api.responses.internal.CompatibleUsersResponse
import com.soundhub.data.api.responses.internal.ErrorResponse
import com.soundhub.data.api.responses.internal.HttpResult
import com.soundhub.data.api.responses.internal.UserExistenceResponse
import com.soundhub.data.api.services.UserService
import com.soundhub.data.model.User
import com.soundhub.domain.repository.BaseRepository
import com.soundhub.domain.repository.UserRepository
import com.soundhub.domain.usecases.user.LoadAllUserDataUseCase
import com.soundhub.utils.enums.ContentTypes
import com.soundhub.utils.enums.UriScheme
import com.soundhub.utils.lib.HttpUtils
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Response
import java.util.UUID
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
	private val userService: UserService,
	private val loadAllUserDataUseCase: LoadAllUserDataUseCase,
	private val context: Context,
	private val gson: Gson
) : UserRepository, BaseRepository(gson, context) {
	override suspend fun getUserById(id: UUID?): HttpResult<User?> {
		try {
			val response: Response<User?> = userService.getUserById(id)
			Log.d("UserRepository", "getUserById[1]: $response")

			return handleResponse<User?>(response) {
				val user: User? = response.body()
				user?.let { loadAllUserDataUseCase(user) }
				Log.d("UserRepository", "getUserById[2]: user = $user")

				return@handleResponse HttpResult.Success(body = user)
			}

		} catch (e: Exception) {
			Log.e("UserRepository", "getUserById[3]: ${e.stackTraceToString()}")
			return handleException(e)
		}
	}

	override suspend fun getCurrentUser(): HttpResult<User?> {
		try {
			val response: Response<User?> = userService.getCurrentUser()
			Log.d("UserRepository", "getCurrentUser[1]: $response")


			return handleResponse<User?>(response) {
				val user: User? = response.body()
				user?.let { loadAllUserDataUseCase(user) }

				return@handleResponse HttpResult.Success(body = user)
			}

		} catch (e: Exception) {
			Log.e("UserRepository", "getCurrentUser[2]: ${e.stackTraceToString()}")
			return handleException(e)
		}
	}

	override suspend fun updateUser(user: User?): HttpResult<User> {
		try {
			val avatarUri: Uri? = user?.avatarUrl?.toUri()
			var avatarFormData: MultipartBody.Part? = null

			// if user has an avatar with path from server we do not a form data
			if (avatarUri?.scheme != UriScheme.HTTP.scheme)
				avatarFormData = HttpUtils.prepareMediaFormData(user?.avatarUrl, context)

			val userRequestBody: RequestBody = gson.toJson(user)
				.toRequestBody(ContentTypes.JSON.type.toMediaTypeOrNull())

			val response: Response<User> = userService.updateUserById(
				id = user?.id,
				user = userRequestBody,
				userAvatar = avatarFormData,
			)
			Log.d("UserRepository", "updateUserById[1]: $response")
			return handleResponse(response)
		} catch (e: Exception) {
			Log.e("UserRepository", "updateUserById[2]: ${e.stackTraceToString()}")
			return HttpResult.Error(
				errorBody = ErrorResponse(detail = e.localizedMessage),
				throwable = e
			)
		}
	}

	override suspend fun addFriend(friendId: UUID): HttpResult<User> {
		try {
			val response: Response<User> = userService.addFriend(friendId)
			Log.d("UserRepository", "addFriend[1]: $response")

			return handleResponse(response)
		} catch (e: Exception) {
			Log.e("UserRepository", "addFriend[2]: ${e.stackTraceToString()}")
			return handleException(e)
		}
	}

	override suspend fun deleteFriend(friendId: UUID): HttpResult<User> {
		try {
			val response: Response<User> = userService.deleteFriend(friendId)
			Log.d("UserRepository", "addFriend[1]: $response")

			return handleResponse(response)
		} catch (e: Exception) {
			Log.e("UserRepository", "addFriend[2]: ${e.stackTraceToString()}")
			return handleException(e)
		}
	}

	override suspend fun getRecommendedFriends(): HttpResult<List<User>> {
		try {
			val response: Response<List<User>> = userService.getRecommendedFriends()
			Log.d("UserRepository", "getRecommendedFriends[1]: $response")

			return handleResponse(response)
		} catch (e: Exception) {
			Log.e("UserRepository", "getRecommendedFriends[2]: ${e.stackTraceToString()}")
			return handleException(e)
		}
	}

	override suspend fun getUsersCompatibilityPercentage(
		userIds: List<UUID>
	): HttpResult<CompatibleUsersResponse> {
		try {
			val requestBody = CompatibleUsersRequestBody(userIds)
			val response: Response<CompatibleUsersResponse> =
				userService.getUsersCompatibilityPercentage(requestBody)
			Log.d("UserRepository", "getUsersCompatibilityPercentage[1]: $response")

			return handleResponse(response)
		} catch (e: Exception) {
			Log.e("UserRepository", "getUsersCompatibilityPercentage[2]: ${e.stackTraceToString()}")
			return handleException(e)
		}
	}

	override suspend fun checkUserExistenceByEmail(email: String): HttpResult<UserExistenceResponse> {
		try {
			val response = userService.checkUserExistenceByEmail(email)
			Log.d("UserRepository", "checkUserExistenceByEmail[1]: $response")

			return handleResponse(response)
		} catch (e: Exception) {
			Log.e("UserRepository", "checkUserExistenceByEmail[2]: ${e.stackTraceToString()}")
			return handleException(e)
		}
	}

	override suspend fun getFriendsByUserId(userId: UUID): HttpResult<List<User>> {
		try {
			val response: Response<List<User>> = userService.getFriendsByUserId(userId)
			Log.d("UserRepository", "getFriendsByUserId[1]: $response")

			return handleResponse(response)
		} catch (e: Exception) {
			Log.e("UserRepository", "getFriendsByUserId[2]: ${e.stackTraceToString()}")
			return handleException(e)
		}
	}

	override suspend fun searchUserByFullName(name: String): HttpResult<List<User>> {
		try {
			val response: Response<List<User>> = userService.searchUserByFullName(name)
			Log.d("UserRepository", "searchUserByFullName[1]: $response")

			return handleResponse(response)
		} catch (e: Exception) {
			Log.e("UserRepository", "searchUserByFullName[2]: ${e.stackTraceToString()}")
			return handleException(e)
		}
	}

	override suspend fun toggleUserOnline(): HttpResult<User?> {
		try {
			val response: Response<User?> = userService.toggleUserOnline()
			Log.d("UserRepository", "toggleUserOnline[1]: $response")

			return handleResponse<User?>(response) {
				val user = response.body()
				user?.let { loadAllUserDataUseCase(user) }

				return@handleResponse HttpResult.Success(user)
			}

		} catch (e: Exception) {
			Log.e("UserRepository", "toggleUserOnline[2]: ${e.stackTraceToString()}")
			return handleException(e)
		}
	}
}