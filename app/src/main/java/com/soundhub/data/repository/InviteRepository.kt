package com.soundhub.data.repository

import com.soundhub.data.api.responses.HttpResult
import com.soundhub.data.model.Invite
import java.util.UUID

interface InviteRepository {
    suspend fun createInvite(
        accessToken: String?,
        recipientId: UUID
    ): HttpResult<Invite>

    suspend fun acceptInvite(
        accessToken: String?,
        inviteId: UUID
    ): HttpResult<Invite>

    suspend fun rejectInvite(
        accessToken: String?,
        inviteId: UUID
    ): HttpResult<Invite>

    suspend fun getAllInvites(accessToken: String?): HttpResult<List<Invite>>

    suspend fun deleteInvite(
        accessToken: String?,
        inviteId: UUID
    ): HttpResult<Invite>
}