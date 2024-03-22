package com.soundhub

import com.soundhub.data.api.requests.RegisterRequestBody
import com.soundhub.data.model.Artist
import com.soundhub.data.model.Gender
import com.soundhub.data.model.Genre
import com.soundhub.data.model.User
import com.soundhub.ui.states.RegistrationState
import com.soundhub.ui.states.UserFormState
import com.soundhub.utils.mappers.UserMapper
import org.junit.Test
import org.junit.Assert.*
import org.mapstruct.factory.Mappers
import java.time.LocalDate

class UserMapperTest {
    private val user = User(
        email = "user@mail.com",
        firstName = "Ivan",
        lastName = "Ivanov",
        gender = Gender.MALE,
        country = "Russia",
        city = "Moscow",
        birthday = LocalDate.now(),
        description = "description",
        languages = mutableListOf("Russian"),
        favoriteArtists = mutableListOf(Artist(name = "Rammstein")),
        favoriteGenres = mutableListOf(Genre(name = "Industrial metal")),
    )

    private val registerState: RegistrationState = RegistrationState(
        email = user.email!!,
        password = "11111111",
        firstName = user.firstName,
        lastName = user.lastName,
        gender = user.gender,
        country = user.country,
        city = user.city,
        birthday = user.birthday,
        description = user.description,
        languages = user.languages,
        favoriteGenres = user.favoriteGenres,
        favoriteArtists = user.favoriteArtists,
        id = user.id
    )

    private val userFormState: UserFormState = UserFormState(
        email = user.email!!,
        firstName = user.firstName,
        lastName = user.lastName,
        gender = user.gender,
        country = user.country,
        city = user.city,
        birthday = user.birthday,
        description = user.description,
        languages = user.languages,
        id = user.id
    )

    private val registerRequestBody = RegisterRequestBody(
        email = user.email!!,
        password = "11111111",
        firstName = user.firstName!!,
        lastName = user.lastName!!,
        gender = user.gender,
        country = user.country,
        city = user.city,
        birthday = user.birthday,
        description = user.description,
        languages = user.languages,
        favoriteGenres = user.favoriteGenres,
        favoriteArtists = user.favoriteArtists,
    )

    private val mapper: UserMapper = Mappers.getMapper(UserMapper::class.java)

    @Test
    fun getUserFromRegistrationTest() {
        val result: User = mapper.fromRegistrationState(registerState)
        assertEquals(user, result)
    }

    @Test
    fun getUserFromFormState() {
        val result: User = mapper.mergeUserWithFormState(userFormState, user)
        assertEquals(user, result)
    }

    @Test
    fun getFormStateFromUserInstance() {
        val result: UserFormState = mapper.toFormState(user)
        assertEquals(userFormState, result)
    }

    @Test
    fun getRegisterRequestBody() {
        val result: RegisterRequestBody = mapper.registerStateToRegisterRequestBody(registerState)
        assertEquals(registerRequestBody, result)
    }
}