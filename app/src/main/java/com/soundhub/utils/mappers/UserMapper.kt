package com.soundhub.utils.mappers

import com.soundhub.data.model.User
import com.soundhub.ui.authentication.postregistration.states.RegistrationState
import com.soundhub.ui.states.UserFormState
import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.MappingTarget

@Mapper
interface UserMapper {
    @Mapping(target = "friends", ignore = true)
    fun fromRegistrationState(state: RegistrationState): User
    fun toFormState(user: User?): UserFormState
    fun mergeUserWithFormState(state: UserFormState, @MappingTarget user: User?): User
}