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
import com.soundhub.utils.Constants
import com.soundhub.utils.ContentTypes
import com.soundhub.utils.converters.LocalDateAdapter
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Response
import java.time.LocalDate
import java.util.UUID
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val userService: UserService,
    private val context: Context
): UserRepository {
    private val gson = GsonBuilder()
        .registerTypeAdapter(LocalDate::class.java, LocalDateAdapter())
        .create()

    override suspend fun getUserById(id: UUID?, accessToken: String?): HttpResult<User?> {
        try {
            val response: Response<User?> = userService.getUserById(
                id = id,
                accessToken = HttpUtils.getBearerToken(accessToken)
            )
            Log.d("UserRepository", "getUserById[1]: $response")

            if (!response.isSuccessful) {
                val errorBody: ErrorResponse = gson
                    .fromJson(response.errorBody()?.charStream(), Constants.ERROR_BODY_TYPE)
                    ?: ErrorResponse(
                        status = response.code(),
                        detail = context.getString(R.string.toast_common_error)
                    )

                Log.d("UserRepository", "getUserById[2]: $errorBody")
                return HttpResult.Error(errorBody = errorBody)
            }

            return HttpResult.Success(body = response.body())
        }
        catch (e: Exception) {
            Log.e("UserRepository", "getUserById[3]: ${e.stackTraceToString()}")
            return HttpResult.Error(
                errorBody = ErrorResponse(detail = e.localizedMessage),
                throwable = e
            )
        }
    }

    override suspend fun getCurrentUser(accessToken: String?): HttpResult<User?> {
        try {
            val response: Response<User?> = userService.getCurrentUser(
                accessToken = HttpUtils.getBearerToken(accessToken)
            )
            Log.d("UserRepository", "getCurrentUser[1]: $response")

            if (!response.isSuccessful) {
                val errorBody: ErrorResponse = gson
                    .fromJson(response.errorBody()?.charStream(), Constants.ERROR_BODY_TYPE)
                    ?: ErrorResponse(
                        status = response.code(),
                        detail = context.getString(R.string.toast_common_error)
                    )

                Log.d("UserRepository", "getCurrentUser[2]: ${errorBody.toString()}")
                response.headers()
                return HttpResult.Error(errorBody = errorBody)
            }

            return HttpResult.Success(body = response.body())
        }
        catch (e: Exception) {
            Log.e("UserRepository", "getCurrentUser[3]: ${e.stackTraceToString()}")
            return HttpResult.Error(
                errorBody = ErrorResponse(detail = e.localizedMessage),
                throwable = e
            )
        }
    }

    override suspend fun updateUserById(user: User?, accessToken: String?): HttpResult<User> {
        try {
            val avatarFormData: MultipartBody.Part? = HttpUtils
                .prepareMediaFormData(user?.avatarUrl, context)
            val userRequestBody: RequestBody = gson.toJson(user)
                .toRequestBody(ContentTypes.JSON.type.toMediaTypeOrNull())

            val response: Response<User> = userService.updateUserById(
                id = user?.id,
                user = userRequestBody,
                userAvatar = avatarFormData,
                accessToken = HttpUtils.getBearerToken(accessToken)
            )
            Log.d("UserRepository", "updateUserById[1]: $response")

            if (!response.isSuccessful) {
                val errorBody: ErrorResponse = gson
                    .fromJson(response.errorBody()?.charStream(), Constants.ERROR_BODY_TYPE)
                    ?: ErrorResponse(
                        status = response.code(),
                        detail = context.getString(R.string.toast_update_error)
                    )

                Log.d("UserRepository", "updateUserById[2]: $errorBody")
                return HttpResult.Error(errorBody = errorBody)
            }

            return HttpResult.Success(body = response.body())
        }

        catch (e: Exception) {
            Log.d("UserRepository", "updateUserById[3]: ${e.stackTraceToString()}")
            return HttpResult.Error(
                errorBody = ErrorResponse(detail = e.localizedMessage),
                throwable = e
            )
        }
    }

    override suspend fun addFriend(accessToken: String?, friendId: UUID): HttpResult<User> {
        try {
            val response: Response<User> = userService.addFriend(
                accessToken = HttpUtils.getBearerToken(accessToken),
                friendId = friendId
            )
            Log.d("UserRepository", "addFriend[1]: $response")

            if (!response.isSuccessful) {
                val errorBody: ErrorResponse = gson
                    .fromJson(response.errorBody()?.charStream(), Constants.ERROR_BODY_TYPE)
                    ?: ErrorResponse(
                        status = response.code(),
                        detail = context.getString(R.string.toast_common_error)
                    )

                Log.d("UserRepository", "addFriend[2]: $errorBody")
                response.headers()
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

    override suspend fun deleteFriend(accessToken: String?, friendId: UUID): HttpResult<User> {
        try {
            val response: Response<User> = userService.deleteFriend(
                accessToken = HttpUtils.getBearerToken(accessToken),
                friendId = friendId
            )
            Log.d("UserRepository", "addFriend[1]: $response")

            if (!response.isSuccessful) {
                val errorBody: ErrorResponse = gson
                    .fromJson(response.errorBody()?.charStream(), Constants.ERROR_BODY_TYPE)
                    ?: ErrorResponse(
                        status = response.code(),
                        detail = context.getString(R.string.toast_common_error)
                    )

                Log.d("UserRepository", "addFriend[2]: $errorBody")
                response.headers()
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

    override suspend fun getRecommendedFriends(
        accessToken: String?,
        userId: UUID?
    ): HttpResult<List<User>> {
        try {
            val response: Response<List<User>> = userService.getRecommendedFriends(
                accessToken = HttpUtils.getBearerToken(accessToken),
                userId = userId
            )
            Log.d("UserRepository", "getRecommendedFriends[1]: $response")

            if (!response.isSuccessful) {
                val errorBody: ErrorResponse = gson
                    .fromJson(response.errorBody()?.charStream(), Constants.ERROR_BODY_TYPE)
                    ?: ErrorResponse(
                        status = response.code(),
                        detail = context.getString(R.string.toast_common_error)
                    )

                Log.d("UserRepository", "getRecommendedFriends[2]: $errorBody")
                response.headers()
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
}