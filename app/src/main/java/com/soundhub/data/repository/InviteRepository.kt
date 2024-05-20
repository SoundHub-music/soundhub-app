package com.soundhub.data.repository

import com.soundhub.data.api.responses.HttpResult
import com.soundhub.data.model.Invite
import com.soundhub.utils.ApiEndpoints
import com.soundhub.utils.HttpUtils
import retrofit2.http.Header
import retrofit2.http.Path
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

//    suspend fun getAllInvitesBySenderId(
//        accessToken: String?,
//        senderId: UUID
//    ): HttpResult<List<Invite>>
    suspend fun getInviteBySenderAndRecipientId(
        @Header(HttpUtils.AUTHORIZATION_HEADER)
        accessToken: String?,
        @Path(ApiEndpoints.Invites.SENDER_ID_DYNAMIC_PARAM)
        senderId: UUID?,
        @Path(ApiEndpoints.Invites.RECIPIENT_ID_DYNAMIC_PARAM)
        recipientId: UUID?
    ): HttpResult<Invite?>

    suspend fun deleteInvite(
        accessToken: String?,
        inviteId: UUID
    ): HttpResult<Invite>
}