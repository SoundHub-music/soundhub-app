package com.soundhub.data.api

import com.soundhub.data.model.Invite
import com.soundhub.utils.ApiEndpoints
import com.soundhub.utils.HttpUtils
import retrofit2.Response
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path
import java.util.UUID

interface InviteService {
    @POST(ApiEndpoints.Invites.CREATE_INVITE)
    suspend fun createInvite(
        @Header(HttpUtils.AUTHORIZATION_HEADER)
        accessToken: String?,
        @Path(ApiEndpoints.Invites.RECIPIENT_ID_DYNAMIC_PARAM)
        recipientId: UUID?
    ): Response<Invite>

    @POST(ApiEndpoints.Invites.ACCEPT_INVITE)
    suspend fun acceptInvite(
        @Header(HttpUtils.AUTHORIZATION_HEADER)
        accessToken: String?,
        @Path(ApiEndpoints.Invites.INVITE_ID_DYNAMIC_PARAM)
        inviteId: UUID
    ): Response<Invite>

    @POST(ApiEndpoints.Invites.REJECT_INVITE)
    suspend fun rejectInvite(
        @Header(HttpUtils.AUTHORIZATION_HEADER)
        accessToken: String?,
        @Path(ApiEndpoints.Invites.INVITE_ID_DYNAMIC_PARAM)
        inviteId: UUID
    ): Response<Invite>

    @GET(ApiEndpoints.Invites.INVITES_ENDPOINT)
    suspend fun getAllInvites(
        @Header(HttpUtils.AUTHORIZATION_HEADER)
        accessToken: String?
    ): Response<List<Invite>>

    @GET(ApiEndpoints.Invites.GET_ALL_INVITES_BY_SENDER_ID)
    suspend fun getAllInvitesBySenderId(
        @Header(HttpUtils.AUTHORIZATION_HEADER)
        accessToken: String?,
        @Path(ApiEndpoints.Invites.SENDER_ID_DYNAMIC_PARAM)
        senderId: UUID?
    ): Response<List<Invite>>

    @DELETE(ApiEndpoints.Invites.DELETE_INVITE)
    suspend fun deleteInvite(
        @Header(HttpUtils.AUTHORIZATION_HEADER)
        accessToken: String?,
        @Path(ApiEndpoints.Invites.INVITE_ID_DYNAMIC_PARAM)
        inviteId: UUID
    ): Response<Invite>
}