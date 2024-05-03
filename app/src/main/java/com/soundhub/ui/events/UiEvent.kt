package com.soundhub.ui.events

import com.soundhub.Route
import com.soundhub.data.model.User
import com.soundhub.utils.UiText

sealed class UiEvent {
    data object PopBackStack: UiEvent()
    data object SearchButtonClick: UiEvent()
    data class Navigate(val route: Route): UiEvent()
    data class ShowToast(val uiText: UiText): UiEvent()
    data object UpdateUserAction: UiEvent()
    data class UpdateUserInstance(val user: User): UiEvent()
}