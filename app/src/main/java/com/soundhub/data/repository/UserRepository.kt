package com.soundhub.data.repository

import com.soundhub.data.api.responses.HttpResult
import com.soundhub.data.model.User
import java.util.UUID

interface UserRepository {
    suspend fun getUserById(id: UUID?, accessToken: String?): HttpResult<User?>
    suspend fun getCurrentUser(accessToken: String?): HttpResult<User?>
    suspend fun updateUserById(user: User?, accessToken: String?): HttpResult<User>
    suspend fun addFriend(accessToken: String?, friendId: UUID): HttpResult<User>
    suspend fun deleteFriend(accessToken: String?, friendId: UUID): HttpResult<User>
    suspend fun getRecommendedFriends(accessToken: String?, userId: UUID?): HttpResult<List<User>>
}