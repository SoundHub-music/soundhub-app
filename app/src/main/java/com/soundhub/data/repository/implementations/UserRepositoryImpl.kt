package com.soundhub.data.repository.implementations

import android.util.Log
import com.google.gson.Gson
import com.soundhub.data.api.responses.HttpResult
import com.soundhub.data.model.User
import com.soundhub.data.api.UserApi
import com.soundhub.data.api.responses.ErrorResponse
import com.soundhub.data.repository.UserRepository
import com.soundhub.utils.Constants
import retrofit2.Response
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val userApi: UserApi
): UserRepository {
    override suspend fun getUserById(id: String?, token: String?): HttpResult<User?> {
        try {
            val response: Response<User?> = userApi.getUserById(id, token)
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

    override suspend fun getCurrentUser(token: String?): HttpResult<User?> {
        try {
            val currentUserResponse: Response<User?> = userApi.getCurrentUser("Bearer $token")
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
}