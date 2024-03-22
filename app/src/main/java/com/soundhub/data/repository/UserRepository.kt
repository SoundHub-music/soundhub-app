package com.soundhub.data.repository

import com.soundhub.data.api.responses.HttpResult
import com.soundhub.data.model.User

interface UserRepository {
    suspend fun getUserById(id: String?, accessToken: String?): HttpResult<User?>
    suspend fun getCurrentUser(accessToken: String?): HttpResult<User?>
    suspend fun updateUserById(user: User?, accessToken: String?): HttpResult<User>
}