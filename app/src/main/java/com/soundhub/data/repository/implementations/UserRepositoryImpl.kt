package com.soundhub.data.repository.implementations

import com.soundhub.data.model.ApiResult
import com.soundhub.data.model.User
import com.soundhub.data.api.UserApi
import com.soundhub.data.repository.UserRepository
import retrofit2.Response
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val userApi: UserApi
): UserRepository {
    override suspend fun getUserById(id: String, token: String?): ApiResult<User?> {
        val response: Response<User?> = userApi.getUserById(id, token)
        if (!response.isSuccessful) {
            return ApiResult.Error(
                message = response.message(),
                code = response.code()
            )
        }

        return ApiResult.Success(data = response.body())
    }

    override suspend fun getCurrentUser(token: String?): ApiResult<User?> {
        try {
            val currentUserResponse: Response<User?> = userApi.getCurrentUser("Bearer $token")
            if (!currentUserResponse.isSuccessful)
                return ApiResult.Error(
                    message = currentUserResponse.message(),
                    code = currentUserResponse.code()
                )

            return ApiResult.Success(data = currentUserResponse.body())
        }
        catch (e: Exception) {
            return ApiResult.Error(message = e.message)
        }
    }
}