package com.soundhub.data.model

import com.soundhub.ui.authentication.state.RegistrationState
import java.time.LocalDate
import java.util.UUID

data class User(
    var id: UUID = UUID.randomUUID(),
    var gender: Gender = Gender.UNKNOWN,
    var avatarUrl: String? = null,
    var email: String? = "",
    var firstName: String? = "",
    var lastName: String? = "",
    var country: String? = "",
    var birthday: LocalDate? = null,
    var city: String? = "",
    var description: String? = "",
    var languages: List<String> = emptyList()

) {
    constructor(registerState: RegistrationState?) : this() {
        id = registerState?.id ?: UUID.randomUUID()
        email = registerState?.email ?: ""
        firstName = registerState?.firstName ?: ""
        lastName = registerState?.lastName ?: ""
        country = registerState?.country ?: ""
        birthday = registerState?.birthday
        city = registerState?.city ?: ""
        description = registerState?.description ?: ""
    }
}