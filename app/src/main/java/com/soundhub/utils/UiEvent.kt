package com.soundhub.utils

sealed class UiEvent {
    object PopBackStack: UiEvent()
    data class Navigate(val route: Routes): UiEvent()
    data class ShowToast(
        val message: String,
        val action: String? = null
    ): UiEvent()
}