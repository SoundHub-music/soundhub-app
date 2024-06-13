package com.soundhub.data.repository

import com.soundhub.data.api.responses.CompatibleUsersResponse
import com.soundhub.data.api.responses.HttpResult
import com.soundhub.data.model.User
import java.util.UUID

interface UserRepository {
    suspend fun getUserById(id: UUID?): HttpResult<User?>
    suspend fun getCurrentUser(): HttpResult<User?>
    suspend fun updateUserById(user: User?): HttpResult<User>
    suspend fun addFriend(friendId: UUID): HttpResult<User>
    suspend fun deleteFriend(friendId: UUID): HttpResult<User>
    suspend fun getRecommendedFriends(): HttpResult<List<User>>
    suspend fun getUsersCompatibilityPercentage(
        userIds: List<UUID>
    ): HttpResult<CompatibleUsersResponse>
    suspend fun getFriendsByUserId(userId: UUID): HttpResult<List<User>>
    suspend fun searchUserByFullName(name: String): HttpResult<List<User>>
    suspend fun toggleUserOnline(): HttpResult<User?>
}