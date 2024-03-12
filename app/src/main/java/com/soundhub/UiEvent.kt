package com.soundhub

import com.soundhub.data.model.User
import com.soundhub.utils.UiText

sealed class UiEvent {
    object PopBackStack: UiEvent()
    object SearchButtonClick: UiEvent()
    data class Navigate(val route: Route): UiEvent() {
        override fun toString(): String {
            return "Route{\n" +
                    "route: ${route.route}\n}"
        }
    }

    data class ShowToast(val uiText: UiText): UiEvent()
    data class UpdateCurrentUser(val user: User): UiEvent()
}