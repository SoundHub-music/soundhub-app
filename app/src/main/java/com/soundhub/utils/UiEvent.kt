package com.soundhub.utils

sealed class UiEvent {
    object PopBackStack: UiEvent()
    data class Navigate(val route: Routes): UiEvent() {
        override fun toString(): String {
            return "Route{\n" +
                    "route: ${route.route}\n" +
                    "pageName: ${route.pageName}\n" +
                    "}"
        }
    }
    data class ShowToast(
        val message: String,
        val action: String? = null
    ): UiEvent()
}