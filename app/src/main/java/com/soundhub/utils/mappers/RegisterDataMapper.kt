package com.soundhub.utils.mappers

import com.soundhub.data.api.requests.RegisterRequestBody
import com.soundhub.ui.authentication.postregistration.states.RegistrationState
import org.mapstruct.Mapper

@Mapper
interface RegisterDataMapper {
    fun registerStateToRegisterRequestBody(registrationState: RegistrationState?): RegisterRequestBody
}