package com.soundhub.ui.notifications

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.soundhub.R
import com.soundhub.data.enums.ApiStatus
import com.soundhub.data.model.Invite
import com.soundhub.data.model.User
import com.soundhub.data.repository.InviteRepository
import com.soundhub.ui.events.UiEvent
import com.soundhub.ui.states.UiState
import com.soundhub.ui.viewmodels.UiStateDispatcher
import com.soundhub.utils.UiText
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class NotificationViewModel @Inject constructor(
    private val inviteRepository: InviteRepository,
    private val uiStateDispatcher: UiStateDispatcher,
): ViewModel() {
    private val uiState: Flow<UiState> = uiStateDispatcher.uiState

    private val _notificationUiState = MutableStateFlow(NotificationUiState())
    val notificationUiState = _notificationUiState.asStateFlow()

    init { loadInvites() }

    fun loadInvites() {
        viewModelScope.launch(Dispatchers.IO) {
            _notificationUiState.update { it.copy(status = ApiStatus.LOADING) }

            inviteRepository.getAllInvites()
                .onSuccess { response ->
                    val invites: List<Invite> = response.body.orEmpty()
                    _notificationUiState.update {
                        it.copy(
                            notifications = invites,
                            status = ApiStatus.SUCCESS
                        )
                    }
                }
                .onFailure { error ->
                    val errorEvent: UiEvent = UiEvent.Error(error.errorBody, error.throwable)
                    uiStateDispatcher.sendUiEvent(errorEvent)
                    _notificationUiState.update { it.copy(status = ApiStatus.ERROR) }
                }
        }
    }

    fun acceptInvite(invite: Invite) = viewModelScope.launch(Dispatchers.IO) {
        val toastText = UiText.StringResource(R.string.toast_invite_accepted_successfully)
        val uiEvent = UiEvent.ShowToast(toastText)

        inviteRepository.acceptInvite(invite.id)
            .onSuccess {
                deleteNotificationById(invite.id)
                val authorizedUser: User? = uiState.map { it.authorizedUser }
                    .firstOrNull()
                authorizedUser?.let { user ->
                    with(user) {
                        this.friends += invite.sender
                        uiStateDispatcher.setAuthorizedUser(this)
                    }
                }
            }
            .onFailure { error ->
                val errorEvent: UiEvent = UiEvent.Error(
                    error.errorBody,
                    error.throwable,
                    R.string.toast_accept_invite_error
                )
                uiStateDispatcher.sendUiEvent(errorEvent)
            }
            .finally {
                uiStateDispatcher.sendUiEvent(uiEvent)
            }
    }

    fun rejectInvite(invite: Invite) = viewModelScope.launch(Dispatchers.IO) {
        val toastText = UiText.StringResource(R.string.toast_invite_rejected_successfully)
        val uiEvent = UiEvent.ShowToast(toastText)

        inviteRepository.rejectInvite(invite.id)
            .onSuccess {
                deleteNotificationById(invite.id)
            }
            .onFailure { error ->
                val errorEvent: UiEvent = UiEvent.Error(
                    error.errorBody,
                    error.throwable,
                    R.string.toast_reject_invite_error
                )
                uiStateDispatcher.sendUiEvent(errorEvent)
            }
            .finally {
                uiStateDispatcher.sendUiEvent(uiEvent)
            }
    }

    private fun deleteNotificationById(notificationId: UUID) = _notificationUiState.update {
        val notifications = it.notifications.filter { n -> n.id != notificationId }
        it.copy(notifications = notifications)
    }
}