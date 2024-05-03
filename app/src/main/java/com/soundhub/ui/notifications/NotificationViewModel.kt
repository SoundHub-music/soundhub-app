package com.soundhub.ui.notifications

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.soundhub.R
import com.soundhub.data.datastore.UserCredsStore
import com.soundhub.data.datastore.UserPreferences
import com.soundhub.data.enums.ApiStatus
import com.soundhub.data.repository.InviteRepository
import com.soundhub.ui.events.UiEvent
import com.soundhub.ui.viewmodels.UiStateDispatcher
import com.soundhub.utils.UiText
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class NotificationViewModel @Inject constructor(
    private val inviteRepository: InviteRepository,
    private val uiStateDispatcher: UiStateDispatcher,
    userCredsStore: UserCredsStore
): ViewModel() {
    private val userCreds: Flow<UserPreferences> = userCredsStore.getCreds()
    val notificationUiState = MutableStateFlow(NotificationUiState())

    init { loadInvites() }

    fun loadInvites() = viewModelScope.launch(Dispatchers.IO) {
        notificationUiState.update { it.copy(status = ApiStatus.LOADING) }
        userCreds.collect { creds ->
            inviteRepository.getAllInvites(creds.accessToken)
                .onSuccess { response ->
                    val invites = response.body ?: emptyList()
                    notificationUiState.update {
                        it.copy(
                            notifications = invites,
                            status = ApiStatus.SUCCESS
                        )
                    }
                }
                .onFailure {
                    notificationUiState.update {
                     it.copy(status = ApiStatus.ERROR)
                    }
                }
        }
    }

    fun acceptInvite(inviteId: UUID) = viewModelScope.launch(Dispatchers.IO) {
        val toastText = UiText.StringResource(R.string.toast_invite_accepted_successfully)
        val uiEvent = UiEvent.ShowToast(toastText)

        val creds: UserPreferences? = userCreds.firstOrNull()
        inviteRepository.acceptInvite(
            accessToken = creds?.accessToken,
            inviteId = inviteId
        )
            .onSuccess {
                deleteNotificationById(inviteId)
            }
            .onFailure {
                toastText.srcId = R.string.toast_accept_invite_error
            }
            .finally {
                uiStateDispatcher.sendUiEvent(uiEvent)
            }
    }

    fun rejectInvite(inviteId: UUID) = viewModelScope.launch(Dispatchers.IO) {
        val toastText = UiText.StringResource(R.string.toast_invite_rejected_successfully)
        val uiEvent = UiEvent.ShowToast(toastText)

        val creds: UserPreferences? = userCreds.firstOrNull()
        inviteRepository.rejectInvite(
            accessToken = creds?.accessToken,
            inviteId = inviteId
        )
            .onSuccess {
                deleteNotificationById(inviteId)
            }
            .onFailure {
                toastText.srcId = R.string.toast_reject_invite_error
            }
            .finally {
                uiStateDispatcher.sendUiEvent(uiEvent)
            }
    }

    private fun deleteNotificationById(notificationId: UUID) = notificationUiState.update {
        val notifications = it.notifications.filter { n -> n.id != notificationId }
        it.copy(notifications = notifications)
    }
}