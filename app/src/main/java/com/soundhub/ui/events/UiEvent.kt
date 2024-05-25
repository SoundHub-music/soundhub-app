package com.soundhub.ui.events

import androidx.annotation.StringRes
import com.soundhub.Route
import com.soundhub.data.api.responses.ErrorResponse
import com.soundhub.utils.UiText

sealed class UiEvent {
    data object PopBackStack: UiEvent()
    data object SearchButtonClick: UiEvent()
    data class Navigate(val route: Route): UiEvent()
    data class ShowToast(val uiText: UiText): UiEvent()
    data object UpdateUserAction: UiEvent()
    data class Error(
        val response: ErrorResponse,
        val throwable: Throwable? = null,
        @StringRes
        val customMessageStringRes: Int? = null
    ): UiEvent()
}