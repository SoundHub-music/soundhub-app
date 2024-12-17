package com.soundhub.domain.states

import com.soundhub.data.enums.ApiStatus
import com.soundhub.domain.model.Notification
import com.soundhub.domain.states.interfaces.UiStatusState

data class NotificationUiState(
	val notifications: List<Notification> = emptyList(),
	override val status: ApiStatus = ApiStatus.LOADING,
) : UiStatusState
