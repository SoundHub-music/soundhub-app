package com.soundhub.data.api

import com.soundhub.data.model.Invite
import com.soundhub.utils.ApiEndpoints.Invites.ACCEPT_INVITE
import com.soundhub.utils.ApiEndpoints.Invites.CREATE_INVITE
import com.soundhub.utils.ApiEndpoints.Invites.DELETE_INVITE
import com.soundhub.utils.ApiEndpoints.Invites.GET_INVITE_BY_SENDER_AND_RECIPIENT
import com.soundhub.utils.ApiEndpoints.Invites.INVITES_ENDPOINT
import com.soundhub.utils.ApiEndpoints.Invites.INVITE_ID_DYNAMIC_PARAM
import com.soundhub.utils.ApiEndpoints.Invites.RECIPIENT_ID_DYNAMIC_PARAM
import com.soundhub.utils.ApiEndpoints.Invites.REJECT_INVITE
import com.soundhub.utils.ApiEndpoints.Invites.SENDER_ID_DYNAMIC_PARAM
import retrofit2.Response
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import java.util.UUID

interface InviteService {
    @POST(CREATE_INVITE)
    suspend fun createInvite(
        @Path(RECIPIENT_ID_DYNAMIC_PARAM)
        recipientId: UUID?
    ): Response<Invite>

    @POST(ACCEPT_INVITE)
    suspend fun acceptInvite(
        @Path(INVITE_ID_DYNAMIC_PARAM)
        inviteId: UUID
    ): Response<Invite>

    @POST(REJECT_INVITE)
    suspend fun rejectInvite(
        @Path(INVITE_ID_DYNAMIC_PARAM)
        inviteId: UUID
    ): Response<Invite>

    @GET(INVITES_ENDPOINT)
    suspend fun getAllInvites(): Response<List<Invite>>

    @GET(GET_INVITE_BY_SENDER_AND_RECIPIENT)
    suspend fun getInviteBySenderAndRecipientId(
        @Path(SENDER_ID_DYNAMIC_PARAM)
        senderId: UUID?,
        @Path(RECIPIENT_ID_DYNAMIC_PARAM)
        recipientId: UUID?
    ): Response<Invite?>

    @DELETE(DELETE_INVITE)
    suspend fun deleteInvite(
        @Path(INVITE_ID_DYNAMIC_PARAM)
        inviteId: UUID
    ): Response<Invite>
}