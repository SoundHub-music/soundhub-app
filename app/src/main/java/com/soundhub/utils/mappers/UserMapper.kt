package com.soundhub.utils.mappers

import com.soundhub.data.model.User
import com.soundhub.ui.authentication.registration.states.RegistrationState
import com.soundhub.ui.states.UserFormState
import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.MappingTarget
import org.mapstruct.factory.Mappers

@Mapper
interface UserMapper {
    @Mapping(target = "friends", ignore = true)
    @Mapping(target = "favoriteArtistsIds", ignore = true)
    fun fromRegistrationState(state: RegistrationState): User

    @Mapping(target = "firstNameValid", ignore = true)
    @Mapping(target = "lastNameValid", ignore = true)
    @Mapping(target = "birthdayValid", ignore = true)
    fun toFormState(user: User): UserFormState

    @Mapping(target = "friends", ignore = true)
    @Mapping(target = "favoriteGenres", ignore = true)
    @Mapping(target = "favoriteArtists", ignore = true)
    @Mapping(target = "favoriteArtistsIds", ignore = true)
    fun mergeUserWithFormState(state: UserFormState, @MappingTarget user: User): User

    companion object {
        val impl: UserMapper = Mappers.getMapper(UserMapper::class.java)
    }
}