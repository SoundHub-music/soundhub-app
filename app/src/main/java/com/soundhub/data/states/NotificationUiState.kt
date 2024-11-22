package com.soundhub.data.states

import com.soundhub.data.enums.ApiStatus
import com.soundhub.data.model.Notification
import com.soundhub.data.states.interfaces.UiStatusState

data class NotificationUiState(
	val notifications: List<Notification> = emptyList(),
	override val status: ApiStatus = ApiStatus.LOADING,
) : UiStatusState
