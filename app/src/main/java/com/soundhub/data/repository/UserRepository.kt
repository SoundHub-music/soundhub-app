package com.soundhub.data.repository

import com.soundhub.data.api.responses.HttpResult
import com.soundhub.data.model.User

interface UserRepository {
    suspend fun getUserById(id: String?, token: String?): HttpResult<User?>
    suspend fun getCurrentUser(token: String?): HttpResult<User?>
}