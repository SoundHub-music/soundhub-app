package com.soundhub.data.repository.implementations

import android.content.Context
import android.net.Uri
import android.util.Log
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.soundhub.data.api.responses.HttpResult
import com.soundhub.data.model.User
import com.soundhub.data.api.UserApi
import com.soundhub.data.api.responses.ErrorResponse
import com.soundhub.data.repository.FileRepositoryUtils
import com.soundhub.data.repository.UserRepository
import com.soundhub.utils.Constants
import com.soundhub.utils.MediaTypes
import com.soundhub.utils.converters.LocalDateAdapter
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Response
import java.io.File
import java.time.LocalDate
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val userApi: UserApi,
    private val context: Context
): UserRepository, FileRepositoryUtils {
    override suspend fun getUserById(id: String?, accessToken: String?): HttpResult<User?> {
        try {
            val response: Response<User?> = userApi.getUserById(id, accessToken)
            Log.d("UserRepository", "getUserById[1]: $response")

            if (!response.isSuccessful) {
                val errorBody: ErrorResponse? = Gson()
                    .fromJson(response.errorBody()?.charStream(), Constants.ERROR_BODY_TYPE)
                Log.d("UserRepository", "getUserById[2]: ${errorBody.toString()}")
                return HttpResult.Error(errorBody = errorBody)
            }

            return HttpResult.Success(body = response.body())
        }
        catch (e: Exception) {
            Log.e("UserRepository", "getUserById[3]: ${e.stackTraceToString()}")
            return HttpResult.Error(errorBody = ErrorResponse(detail = e.message))
        }
    }

    override suspend fun getCurrentUser(accessToken: String?): HttpResult<User?> {
        try {
            val currentUserResponse: Response<User?> = userApi.getCurrentUser("Bearer $accessToken")
            Log.d("UserRepository", "getCurrentUser[1]: $currentUserResponse")

            if (!currentUserResponse.isSuccessful) {
                val errorBody: ErrorResponse? = Gson()
                    .fromJson(currentUserResponse.errorBody()?.charStream(), Constants.ERROR_BODY_TYPE)
                Log.d("UserRepository", "getCurrentUser[2]: ${errorBody.toString()}")
                currentUserResponse.headers()
                return HttpResult.Error(errorBody = errorBody)
            }

            return HttpResult.Success(body = currentUserResponse.body())
        }
        catch (e: Exception) {
            Log.e("UserRepository", "getCurrentUser[3]: ${e.stackTraceToString()}")
            return HttpResult.Error(errorBody = ErrorResponse(detail = e.message), throwable = e)
        }
    }

    override suspend fun updateUserById(user: User?, accessToken: String?): HttpResult<User> {
        try {
            val gson = GsonBuilder()
                .registerTypeAdapter(LocalDate::class.java, LocalDateAdapter())
                .create()

            val tempFile: File? = createTempMediaFile(
                imageUri = Uri.parse(user?.avatarUrl).path,
                context = context
            )

            Log.d("UserRepository", "user: ${user.toString()}")
            Log.d("UserRepository", "updateUserById[0]: $tempFile")

            val avatarFormData: MultipartBody.Part? = getImageFormData(tempFile, context)
            val userRequestBody = gson.toJson(user)
                .toRequestBody(MediaTypes.JSON.type.toMediaTypeOrNull())

            val updateResponse: Response<User> = userApi.updateUserById(
                id = user?.id?.toString(),
                user = userRequestBody,
                userAvatar = avatarFormData,
                accessToken = "Bearer $accessToken"
            )
            Log.d("UserRepository", "updateUserById[1]: $updateResponse")

            if (!updateResponse.isSuccessful) {
                val errorBody: ErrorResponse? = Gson()
                    .fromJson(updateResponse.errorBody()?.charStream(), Constants.ERROR_BODY_TYPE)
                Log.d("UserRepository", "updateUserById[2]: ${errorBody.toString()}")

                return HttpResult.Error(errorBody = errorBody)
            }

            return HttpResult.Success(body = updateResponse.body())
        }

        catch (e: Exception) {
            Log.d("UserRepository", "updateUserById[3]: ${e.stackTraceToString()}")
            return HttpResult.Error(throwable = e)
        }
    }
}