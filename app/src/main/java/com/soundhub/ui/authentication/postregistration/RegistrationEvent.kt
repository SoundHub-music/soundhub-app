package com.soundhub.ui.authentication.postregistration

import com.soundhub.data.model.User

sealed class RegistrationEvent {
    data class OnRegister(val user: User): RegistrationEvent()
    object OnChooseGenres: RegistrationEvent()
    object OnChooseArtists: RegistrationEvent()
    object OnFillUserData: RegistrationEvent()
}