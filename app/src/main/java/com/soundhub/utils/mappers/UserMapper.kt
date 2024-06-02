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
    @Mapping(target = "user.isOnline", ignore = true)
    @Mapping(target = "user.lastOnline", ignore = true)
    fun toFormState(user: User): UserFormState

    @Mapping(target = "friends", ignore = true)
    @Mapping(target = "favoriteGenres", ignore = true)
    @Mapping(target = "favoriteArtists", ignore = true)
    @Mapping(target = "favoriteArtistsIds", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "gender", source = "state.gender")
    @Mapping(target = "avatarUrl", source = "state.avatarUrl")
    @Mapping(target = "email", source = "state.email")
    @Mapping(target = "firstName", source = "state.firstName")
    @Mapping(target = "lastName", source = "state.lastName")
    @Mapping(target = "country", source = "state.country")
    @Mapping(target = "birthday", source = "state.birthday")
    @Mapping(target = "city", source = "state.city")
    @Mapping(target = "description", source = "state.description")
    @Mapping(target = "languages", source = "state.languages")
    fun mergeUserWithFormState(state: UserFormState, @MappingTarget user: User): User

    companion object {
        val impl: UserMapper = Mappers.getMapper(UserMapper::class.java)
    }
}