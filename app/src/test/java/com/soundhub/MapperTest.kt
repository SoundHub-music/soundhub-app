package com.soundhub

import com.soundhub.data.api.requests.RegisterRequestBody
import com.soundhub.data.model.Artist
import com.soundhub.data.model.Gender
import com.soundhub.data.model.Genre
import com.soundhub.data.model.User
import com.soundhub.ui.authentication.registration.states.RegistrationState
import com.soundhub.ui.states.UserFormState
import com.soundhub.utils.mappers.RegisterDataMapper
import com.soundhub.utils.mappers.UserMapper
import org.junit.Test
import org.junit.Assert.*
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
        favoriteArtists = mutableListOf(Artist(title = "Rammstein")),
        favoriteGenres = mutableListOf(Genre(name = "Industrial metal")),
    )

    private val registerState: RegistrationState = RegistrationState(
        email = user.email,
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
        email = user.email,
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
        email = user.email,
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
        favoriteArtistsIds = user.favoriteArtists.map { it.id },
    )


    @Test
    fun getUserFromRegistrationTest() {
        val result: User = UserMapper.impl.fromRegistrationState(registerState)
        assertEquals(user, result)
    }

    @Test
    fun getUserFromFormState() {
        val result: User = UserMapper.impl.mergeUserWithFormState(userFormState, user)
        assertEquals(user, result)
    }

    @Test
    fun getFormStateFromUserInstance() {
        val result: UserFormState = UserMapper.impl.toFormState(user)
        assertEquals(userFormState, result)
    }

    @Test
    fun getRegisterRequestBody() {
        val result: RegisterRequestBody = RegisterDataMapper
            .impl.registerStateToRegisterRequestBody(registerState)
        assertEquals(registerRequestBody, result)
    }
}