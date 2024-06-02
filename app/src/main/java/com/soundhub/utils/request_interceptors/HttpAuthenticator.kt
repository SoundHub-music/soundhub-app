package com.soundhub.utils.request_interceptors

import com.google.gson.Gson
import com.soundhub.data.api.AuthService
import com.soundhub.data.api.requests.RefreshTokenRequestBody
import com.soundhub.data.datastore.UserCredsStore
import com.soundhub.data.datastore.UserPreferences
import com.soundhub.utils.HttpUtils
import com.soundhub.utils.constants.Constants
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.runBlocking
import okhttp3.Authenticator
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Inject

class HttpAuthenticator @Inject constructor(
    private val userCredsStore: UserCredsStore
): Authenticator {
    private val authService: AuthService = buildAuthService()

    private fun buildAuthService(): AuthService {
        val client = OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor())
            .build()

        val retrofit = Retrofit.Builder()
            .client(client)
            .addConverterFactory(GsonConverterFactory.create(Gson()))
            .baseUrl(Constants.SOUNDHUB_API)
            .build()

        return retrofit.create(AuthService::class.java)

    }

    override fun authenticate(route: Route?, response: Response): Request? {
        val userPreferences: UserPreferences = runBlocking { refreshToken() }
        val bearerToken: String = HttpUtils.getBearerToken(userPreferences.accessToken)

        return response.request
            .newBuilder()
            .addHeader(HttpUtils.AUTHORIZATION_HEADER, bearerToken)
            .build()
    }

    private suspend fun refreshToken(): UserPreferences {
        val oldCreds: UserPreferences? = userCredsStore.getCreds().firstOrNull()
        val requestBody = RefreshTokenRequestBody(oldCreds?.refreshToken)
        val userPreferences = UserPreferences()

        val refreshTokenResponse: retrofit2.Response<UserPreferences> = authService.refreshToken(requestBody)

        if (refreshTokenResponse.isSuccessful) {
            val body = refreshTokenResponse.body()
            with(userPreferences) {
                accessToken = body?.accessToken
                refreshToken = body?.refreshToken
            }

            userCredsStore.updateCreds(userPreferences)
        }

        return userPreferences
    }
}