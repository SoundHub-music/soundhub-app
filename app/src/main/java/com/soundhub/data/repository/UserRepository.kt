package com.soundhub.data.repository

import com.soundhub.data.api.responses.HttpResult
import com.soundhub.data.model.User
import java.util.UUID

interface UserRepository {
    suspend fun getUserById(accessToken: String?, id: UUID?): HttpResult<User?>
    suspend fun getCurrentUser(accessToken: String?): HttpResult<User?>
    suspend fun updateUserById(accessToken: String?, user: User?): HttpResult<User>
    suspend fun addFriend(accessToken: String?, friendId: UUID): HttpResult<User>
    suspend fun deleteFriend(accessToken: String?, friendId: UUID): HttpResult<User>
    suspend fun getRecommendedFriends(accessToken: String?): HttpResult<List<User>>
    suspend fun getFriendsByUserId(accessToken: String?, userId: UUID): HttpResult<List<User>>
    suspend fun searchUserByFullName(accessToken: String?, name: String): HttpResult<List<User>>
    suspend fun toggleUserOnline(accessToken: String?): HttpResult<User?>
}