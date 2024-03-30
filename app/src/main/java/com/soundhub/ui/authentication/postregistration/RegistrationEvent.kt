package com.soundhub.ui.authentication.postregistration

import com.soundhub.data.model.User

sealed class RegistrationEvent {
    data class OnRegister(val user: User): RegistrationEvent()
    data object OnChooseGenres: RegistrationEvent()
    data object OnChooseArtists: RegistrationEvent()
    data object OnFillUserData: RegistrationEvent()
}