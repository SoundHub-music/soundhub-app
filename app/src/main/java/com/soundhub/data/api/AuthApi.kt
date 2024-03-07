package com.soundhub.data.api

import com.soundhub.data.datastore.UserPreferences
import com.soundhub.data.model.Artist
import com.soundhub.data.model.Gender
import com.soundhub.data.model.Genre
import com.soundhub.ui.authentication.state.RegistrationState
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
import java.time.LocalDate

interface AuthApi {
    @POST("auth/sign-up")
    suspend fun signUp(@Body userData: RegisterRequestBody): Response<UserPreferences>

    @POST("auth/sign-in")
    suspend fun signIn(@Body userData: SignInRequestBody): Response<UserPreferences>

    @POST("auth/logout")
    suspend fun logout(
        @Body creds: LogoutRequestBody
    ): Response<LogoutResponse>

    @POST("auth/refresh")
    suspend fun refreshToken(@Body refreshToken: RefreshTokenRequestBody): Response<UserPreferences>

}

data class RegisterRequestBody(
    var email: String = "",
    var password: String = "",
    var firstName: String = "",
    var lastName: String = "",
    var birthday: LocalDate? = null,
    var city: String? = null,
    var country: String? = null,
    var gender: Gender = Gender.UNKNOWN,
    var avatarUrl: String? = null,
    var description: String? = "",
    var languages: List<String> = emptyList(),
    var favoriteGenres: List<Genre> = emptyList(),
    var favoriteArtists: List<Artist> = emptyList()
) {
    constructor(registerState: RegistrationState) : this() {
        this.email = registerState.email
        this.password = registerState.password
        this.firstName = registerState.firstName ?: ""
        this.lastName = registerState.lastName ?: ""
        this.birthday = registerState.birthday
        this.city = registerState.city
        this.country = registerState.country
        this.gender = registerState.gender
        this.avatarUrl = registerState.avatarURL
        this.description = registerState.description
//        this.languages TODO: implement languages
//        this.favoriteGenres TODO: implement favorite genres
//        this.favoriteArtists TODO: implement favorite artists
    }

}


data class SignInRequestBody(
    val email: String,
    val password: String
)

data class LogoutRequestBody(
    val accessToken: String?,
    val refreshToken: String?
)

data class LogoutResponse(
    val expiredJwt: String
)

data class RefreshTokenRequestBody(
    val refreshToken: String?
)