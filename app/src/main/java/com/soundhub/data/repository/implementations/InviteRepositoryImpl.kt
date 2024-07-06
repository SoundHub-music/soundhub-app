package com.soundhub.data.repository.implementations

import android.util.Log
import com.google.gson.Gson
import com.soundhub.data.api.services.InviteService
import com.soundhub.data.api.responses.ErrorResponse
import com.soundhub.data.api.responses.HttpResult
import com.soundhub.data.model.Invite
import com.soundhub.data.repository.InviteRepository
import com.soundhub.domain.usecases.user.LoadAllUserDataUseCase
import com.soundhub.utils.constants.Constants
import retrofit2.Response
import java.util.UUID
import javax.inject.Inject

class InviteRepositoryImpl @Inject constructor(
    private val inviteService: InviteService,
    private val loadAllUserDataUseCase: LoadAllUserDataUseCase
): InviteRepository {
    override suspend fun createInvite(recipientId: UUID): HttpResult<Invite> {
        try {
            val response: Response<Invite> = inviteService.createInvite(recipientId)

            Log.d("InviteRepository", "createInvite[1]: $response")

            if (!response.isSuccessful) {
                val errorResponse: ErrorResponse = Gson()
                    .fromJson(response.errorBody()?.charStream(), Constants.ERROR_BODY_TYPE)
                    ?: ErrorResponse(status = response.code())
                Log.e("InviteRepository", "createInvite[2]: $errorResponse")
                return HttpResult.Error(errorBody = errorResponse)
            }

            return HttpResult.Success(body = response.body())
        }
        catch (e: Exception) {
            Log.e("InviteRepository", "createInvite[3]: ${e.stackTraceToString()}")
            return HttpResult.Error(
                throwable = e,
                errorBody = ErrorResponse(detail = e.localizedMessage)
            )
        }
    }

    override suspend fun acceptInvite(inviteId: UUID): HttpResult<Invite> {
        try {
            val response: Response<Invite> = inviteService.acceptInvite(inviteId)

            Log.d("InviteRepository", "acceptInvite[1]: $response")

            if (!response.isSuccessful) {
                val errorResponse: ErrorResponse = Gson()
                    .fromJson(response.errorBody()?.charStream(), Constants.ERROR_BODY_TYPE)

                Log.e("InviteRepository", "acceptInvite[2]: $errorResponse")
                return HttpResult.Error(errorBody = errorResponse)
            }

            return HttpResult.Success(body = response.body())
        }
        catch (e: Exception) {
            Log.e("InviteRepository", "acceptInvite[3]: ${e.stackTraceToString()}")
            return HttpResult.Error(
                throwable = e,
                errorBody = ErrorResponse(detail = e.localizedMessage)
            )
        }
    }

    override suspend fun rejectInvite(inviteId: UUID): HttpResult<Invite> {
        try {
            val response: Response<Invite> = inviteService.rejectInvite(inviteId)

            Log.d("InviteRepository", "rejectInvite[1]: $response")

            if (!response.isSuccessful) {
                val errorResponse: ErrorResponse = Gson()
                    .fromJson(response.errorBody()?.charStream(), Constants.ERROR_BODY_TYPE)

                Log.e("InviteRepository", "rejectInvite[2]: $errorResponse")
                return HttpResult.Error(errorBody = errorResponse)
            }

            return HttpResult.Success(body = response.body())
        }
        catch (e: Exception) {
            Log.e("InviteRepository", "rejectInvite[3]: ${e.stackTraceToString()}")
            return HttpResult.Error(
                throwable = e,
                errorBody = ErrorResponse(detail = e.localizedMessage)
            )
        }
    }

    override suspend fun getAllInvites(): HttpResult<List<Invite>> {
        try {
            val response: Response<List<Invite>> = inviteService.getAllInvites()

            Log.d("InviteRepository", "getAllInvites[1]: $response")

            if (!response.isSuccessful) {
                val errorResponse: ErrorResponse = Gson()
                    .fromJson(response.errorBody()?.charStream(), Constants.ERROR_BODY_TYPE)
                    ?: ErrorResponse(status = response.code())

                Log.e("InviteRepository", "getAllInvites[2]: $errorResponse")
                return HttpResult.Error(errorBody = errorResponse)
            }

            return HttpResult.Success(body = response.body())
        }
        catch (e: Exception) {
            Log.e("InviteRepository", "getAllInvites[3]: ${e.stackTraceToString()}")
            return HttpResult.Error(
                throwable = e,
                errorBody = ErrorResponse(detail = e.localizedMessage)
            )
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

            if (!response.isSuccessful) {
                val errorResponse: ErrorResponse = Gson()
                    .fromJson(response.errorBody()?.charStream(), Constants.ERROR_BODY_TYPE)
                    ?: ErrorResponse(status = response.code())

                Log.e("InviteRepository", "getInviteBySenderAndRecipientId[2]: $errorResponse")

                return HttpResult.Error(errorResponse)
            }

            with(response.body()) {
                this?.let {
                    loadAllUserDataUseCase(it.recipient)
                    loadAllUserDataUseCase(it.sender)
                }
            }

            return HttpResult.Success(response.body())
        }
        catch (e: Exception) {
            Log.e("InviteRepository", "getInviteBySenderAndRecipientId[3]: ${e.stackTraceToString()}")
            return HttpResult.Error(
                throwable = e,
                errorBody = ErrorResponse(detail = e.localizedMessage)
            )
        }
    }

    override suspend fun deleteInvite(inviteId: UUID): HttpResult<Invite> {
        try {
            val response: Response<Invite> = inviteService.deleteInvite(inviteId)

            Log.d("InviteRepository", "deleteInvite[1]: $response")

            if (!response.isSuccessful) {
                val errorResponse: ErrorResponse = Gson()
                    .fromJson(response.errorBody()?.charStream(), Constants.ERROR_BODY_TYPE)
                    ?: ErrorResponse(status = response.code())

                Log.e("InviteRepository", "deleteInvite[2]: $errorResponse")
                return HttpResult.Error(errorBody = errorResponse)
            }

            return HttpResult.Success(body = response.body())
        }
        catch (e: Exception) {
            Log.e("InviteRepository", "deleteInvite[3]: ${e.stackTraceToString()}")
            return HttpResult.Error(
                throwable = e,
                errorBody = ErrorResponse(detail = e.localizedMessage)
            )
        }
    }
}