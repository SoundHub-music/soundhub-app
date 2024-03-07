package com.soundhub.data.repository

import com.soundhub.data.model.ApiResult
import com.soundhub.data.model.User

interface UserRepository {
    suspend fun getUserById(id: String, token: String?): ApiResult<User?>
    suspend fun getCurrentUser(token: String?): ApiResult<User?>
}