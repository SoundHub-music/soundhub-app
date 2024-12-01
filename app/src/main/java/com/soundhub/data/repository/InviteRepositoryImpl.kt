package com.soundhub.data.repository

import android.content.Context
import android.util.Log
import com.google.gson.Gson
import com.soundhub.data.api.responses.internal.HttpResult
import com.soundhub.data.api.services.InviteService
import com.soundhub.data.model.Invite
import com.soundhub.domain.repository.BaseRepository
import com.soundhub.domain.repository.InviteRepository
import com.soundhub.domain.usecases.user.LoadAllUserDataUseCase
import retrofit2.Response
import java.util.UUID
import javax.inject.Inject

class InviteRepositoryImpl @Inject constructor(
	private val inviteService: InviteService,
	private val loadAllUserDataUseCase: LoadAllUserDataUseCase,
	gson: Gson,
	context: Context
) : InviteRepository, BaseRepository(gson, context) {
	override suspend fun createInvite(recipientId: UUID): HttpResult<Invite> {
		try {
			val response: Response<Invite> = inviteService.createInvite(recipientId)

			Log.d("InviteRepository", "createInvite[1]: $response")

			return handleResponse(response)
		} catch (e: Exception) {
			Log.e("InviteRepository", "createInvite[2]: ${e.stackTraceToString()}")
			return handleException(e)
		}
	}

	override suspend fun acceptInvite(inviteId: UUID): HttpResult<Invite> {
		try {
			val response: Response<Invite> = inviteService.acceptInvite(inviteId)

			Log.d("InviteRepository", "acceptInvite[1]: $response")

			return handleResponse(response)
		} catch (e: Exception) {
			Log.e("InviteRepository", "acceptInvite[2]: ${e.stackTraceToString()}")
			return handleException(e)
		}
	}

	override suspend fun rejectInvite(inviteId: UUID): HttpResult<Invite> {
		try {
			val response: Response<Invite> = inviteService.rejectInvite(inviteId)
			Log.d("InviteRepository", "rejectInvite[1]: $response")

			return handleResponse(response)
		} catch (e: Exception) {
			Log.e("InviteRepository", "rejectInvite[2]: ${e.stackTraceToString()}")
			return handleException(e)
		}
	}

	override suspend fun getAllInvites(): HttpResult<List<Invite>> {
		try {
			val response: Response<List<Invite>> = inviteService.getAllInvites()
			Log.d("InviteRepository", "getAllInvites[1]: $response")

			return handleResponse(response)
		} catch (e: Exception) {
			Log.e("InviteRepository", "getAllInvites[2]: ${e.stackTraceToString()}")
			return handleException(e)
		}
	}

	override suspend fun getInviteBySenderAndRecipientId(
		senderId: UUID?,
		recipientId: UUID?
	): HttpResult<Invite?> {
		try {
			val response: Response<Invite?> = inviteService.getInviteBySenderAndRecipientId(
				senderId = senderId,
				recipientId = recipientId
			)
			Log.d("InviteRepository", "getInviteBySenderAndRecipientId[1]: $response")

			return handleResponse(response = response, beforeReturningActions = {
				with(response.body()) {
					this?.let {
						loadAllUserDataUseCase(it.recipient)
						loadAllUserDataUseCase(it.sender)
					}
				}
			})
		} catch (e: Exception) {
			Log.e(
				"InviteRepository",
				"getInviteBySenderAndRecipientId[2]: ${e.stackTraceToString()}"
			)
			return handleException(e)
		}
	}

	override suspend fun deleteInvite(inviteId: UUID): HttpResult<Invite> {
		try {
			val response: Response<Invite> = inviteService.deleteInvite(inviteId)
			Log.d("InviteRepository", "deleteInvite[1]: $response")

			return handleResponse(response)
		} catch (e: Exception) {
			Log.e("InviteRepository", "deleteInvite[3]: ${e.stackTraceToString()}")
			return handleException(e)
		}
	}
}