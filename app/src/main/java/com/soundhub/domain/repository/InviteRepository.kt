package com.soundhub.domain.repository

import com.soundhub.data.api.responses.internal.HttpResult
import com.soundhub.data.model.Invite
import java.util.UUID

interface InviteRepository {
	suspend fun createInvite(recipientId: UUID): HttpResult<Invite>

	suspend fun acceptInvite(inviteId: UUID): HttpResult<Invite>

	suspend fun rejectInvite(inviteId: UUID): HttpResult<Invite>

	suspend fun getAllInvites(): HttpResult<List<Invite>>

	suspend fun getInviteBySenderAndRecipientId(
		senderId: UUID?,
		recipientId: UUID?
	): HttpResult<Invite?>

	suspend fun deleteInvite(inviteId: UUID): HttpResult<Invite>
}