package com.soundhub.data.repository.implementations

import android.content.Context
import android.util.Log
import com.google.gson.GsonBuilder
import com.soundhub.R
import com.soundhub.data.api.responses.HttpResult
import com.soundhub.data.model.User
import com.soundhub.data.api.UserService
import com.soundhub.data.api.responses.ErrorResponse
import com.soundhub.utils.HttpUtils
import com.soundhub.data.repository.UserRepository
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

class UserRepositoryImpl @Inject constructor(
    private val userService: UserService,
    private val loadAllUserDataUseCase: LoadAllUserDataUseCase,
    private val context: Context
): UserRepository {
    private val gson = GsonBuilder()
        .registerTypeAdapter(LocalDate::class.java, LocalDateAdapter())
        .registerTypeAdapter(LocalDateTime::class.java, LocalDateTimeAdapter())
        .create()

    override suspend fun getUserById(id: UUID?): HttpResult<User?> {
        try {
            val response: Response<User?> = userService.getUserById(id)
            Log.d("UserRepository", "getUserById[1]: $response")

            if (!response.isSuccessful) {
                val errorBody: ErrorResponse = gson
                    .fromJson(response.errorBody()?.charStream(), Constants.ERROR_BODY_TYPE)
                    ?: ErrorResponse(
                        status = response.code(),
                        detail = context.getString(R.string.toast_common_error)
                    )

                Log.e("UserRepository", "getUserById[2]: $errorBody")
                return HttpResult.Error(errorBody = errorBody)
            }

            val user: User? = response.body()
            user?.let { loadAllUserDataUseCase(user) }
            Log.d("UserRepository", "getUserById[3]: user = $user")

            return HttpResult.Success(body = user)
        }
        catch (e: Exception) {
            Log.e("UserRepository", "getUserById[4]: ${e.stackTraceToString()}")
            return HttpResult.Error(
                errorBody = ErrorResponse(detail = e.localizedMessage),
                throwable = e
            )
        }
    }

    override suspend fun getCurrentUser(): HttpResult<User?> {
        try {
            val currentUserResponse: Response<User?> = userService.getCurrentUser()

            Log.d("UserRepository", "getCurrentUser[1]: $currentUserResponse")

            if (!currentUserResponse.isSuccessful) {
                val errorBody: ErrorResponse = gson
                    .fromJson(currentUserResponse.errorBody()?.charStream(), Constants.ERROR_BODY_TYPE)
                    ?: ErrorResponse(
                        status = currentUserResponse.code(),
                        detail = context.getString(R.string.toast_common_error)
                    )

                Log.e("UserRepository", "getCurrentUser[2]: $errorBody")
                return HttpResult.Error(errorBody = errorBody)
            }

            val user: User? = currentUserResponse.body()
            user?.let { loadAllUserDataUseCase(user) }

            return HttpResult.Success(body = user)
        }
        catch (e: Exception) {
            Log.e("UserRepository", "getCurrentUser[3]: ${e.stackTraceToString()}")
            return HttpResult.Error(
                errorBody = ErrorResponse(detail = e.localizedMessage),
                throwable = e
            )
        }
    }

    override suspend fun updateUserById(user: User?): HttpResult<User> {
        try {
            val avatarFormData: MultipartBody.Part? = HttpUtils
                .prepareMediaFormData(user?.avatarUrl, context)

            val userRequestBody: RequestBody = gson.toJson(user)
                .toRequestBody(ContentTypes.JSON.type.toMediaTypeOrNull())

            val response: Response<User> = userService.updateUserById(
                id = user?.id,
                user = userRequestBody,
                userAvatar = avatarFormData,
            )
            Log.d("UserRepository", "updateUserById[1]: $response")

            if (!response.isSuccessful) {
                val errorBody: ErrorResponse = gson
                    .fromJson(response.errorBody()?.charStream(), Constants.ERROR_BODY_TYPE)
                    ?: ErrorResponse(
                        status = response.code(),
                        detail = context.getString(R.string.toast_update_error)
                    )

                Log.e("UserRepository", "updateUserById[2]: $errorBody")
                return HttpResult.Error(errorBody = errorBody)
            }

            return HttpResult.Success(body = response.body())
        }

        catch (e: Exception) {
            Log.e("UserRepository", "updateUserById[3]: ${e.stackTraceToString()}")
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

            if (!response.isSuccessful) {
                val errorBody: ErrorResponse = gson
                    .fromJson(response.errorBody()?.charStream(), Constants.ERROR_BODY_TYPE)
                    ?: ErrorResponse(
                        status = response.code(),
                        detail = context.getString(R.string.toast_common_error)
                    )

                Log.e("UserRepository", "addFriend[2]: $errorBody")
                return HttpResult.Error(errorBody = errorBody)
            }

            return HttpResult.Success(body = response.body())
        }
        catch (e: Exception) {
            Log.e("UserRepository", "addFriend[3]: ${e.stackTraceToString()}")
            return HttpResult.Error(
                errorBody = ErrorResponse(detail = e.localizedMessage),
                throwable = e
            )
        }
    }

    override suspend fun deleteFriend(friendId: UUID): HttpResult<User> {
        try {
            val response: Response<User> = userService.deleteFriend(friendId)
            Log.d("UserRepository", "addFriend[1]: $response")

            if (!response.isSuccessful) {
                val errorBody: ErrorResponse = gson
                    .fromJson(response.errorBody()?.charStream(), Constants.ERROR_BODY_TYPE)
                    ?: ErrorResponse(
                        status = response.code(),
                        detail = context.getString(R.string.toast_common_error)
                    )

                Log.e("UserRepository", "addFriend[2]: $errorBody")
                return HttpResult.Error(errorBody = errorBody)
            }

            return HttpResult.Success(body = response.body())
        }
        catch (e: Exception) {
            Log.e("UserRepository", "addFriend[3]: ${e.stackTraceToString()}")
            return HttpResult.Error(
                errorBody = ErrorResponse(detail = e.localizedMessage),
                throwable = e
            )
        }
    }

    override suspend fun getRecommendedFriends(): HttpResult<List<User>> {
        try {
            val response: Response<List<User>> = userService.getRecommendedFriends()
            Log.d("UserRepository", "getRecommendedFriends[1]: $response")

            if (!response.isSuccessful) {
                val errorBody: ErrorResponse = gson
                    .fromJson(response.errorBody()?.charStream(), Constants.ERROR_BODY_TYPE)
                    ?: ErrorResponse(
                        status = response.code(),
                        detail = context.getString(R.string.toast_common_error)
                    )

                Log.e("UserRepository", "getRecommendedFriends[2]: $errorBody")
                return HttpResult.Error(errorBody = errorBody)
            }

            return HttpResult.Success(body = response.body())
        }
        catch (e: Exception) {
            Log.e("UserRepository", "getRecommendedFriends[3]: ${e.stackTraceToString()}")
            return HttpResult.Error(
                errorBody = ErrorResponse(detail = e.localizedMessage),
                throwable = e
            )
        }
    }

    override suspend fun getFriendsByUserId(userId: UUID): HttpResult<List<User>> {
        try {
            val response: Response<List<User>> = userService.getFriendsByUserId(userId)
            Log.d("UserRepository", "getFriendsByUserId[1]: $response")

            if (!response.isSuccessful) {
                val errorResponse: ErrorResponse = gson
                    .fromJson(response.errorBody()?.charStream(), Constants.ERROR_BODY_TYPE)
                    ?: ErrorResponse(status = response.code())

                Log.e("UserRepository", "getFriendsByUserId[2]: $errorResponse")
                return HttpResult.Error(errorBody = errorResponse)
            }

            return HttpResult.Success(response.body())
        }
        catch (e: Exception) {
            Log.e("UserRepository", "getFriendsByUserId[3]: ${e.stackTraceToString()}")
            return HttpResult.Error(
                errorBody = ErrorResponse(detail = e.localizedMessage),
                throwable = e
            )
        }
    }

    override suspend fun searchUserByFullName(name: String): HttpResult<List<User>> {
        try {
            val response: Response<List<User>> = userService.searchUserByFullName(name)

            Log.d("UserRepository", "searchUserByFullName[1]: $response")

            if (!response.isSuccessful) {
                val errorResponse: ErrorResponse = gson
                    .fromJson(response.errorBody()?.charStream(), Constants.ERROR_BODY_TYPE)
                    ?: ErrorResponse(
                        status = response.code(),
                        detail = response.message()
                    )

                Log.e("UserRepository", "searchUserByFullName[2]: $errorResponse")
                return HttpResult.Error(errorResponse)
            }

            return HttpResult.Success(response.body())
        }
        catch (e: Exception) {
            Log.e("UserRepository", "searchUserByFullName[3]: ${e.stackTraceToString()}")
            return HttpResult.Error(
                errorBody = ErrorResponse(detail = e.localizedMessage),
                throwable = e
            )
        }
    }

    override suspend fun toggleUserOnline(): HttpResult<User?> {
        try {
            val response: Response<User?> = userService.toggleUserOnline()

            Log.d("UserRepository", "toggleUserOnline[1]: $response")

            if (!response.isSuccessful) {
                val errorResponse: ErrorResponse = gson
                    .fromJson(response.errorBody()?.charStream(), Constants.ERROR_BODY_TYPE)
                    ?: ErrorResponse(
                        status = response.code(),
                        detail = response.message()
                    )

                Log.e("UserRepository", "toggleUserOnline[2]: $errorResponse")
                return HttpResult.Error(errorResponse)
            }

            val user = response.body()
            user?.let { loadAllUserDataUseCase(user) }

            return HttpResult.Success(user)
        }
        catch (e: Exception) {
            Log.e("UserRepository", "toggleUserOnline[3]: ${e.stackTraceToString()}")
            return HttpResult.Error(
                errorBody = ErrorResponse(detail = e.localizedMessage),
                throwable = e
            )
        }
    }
}