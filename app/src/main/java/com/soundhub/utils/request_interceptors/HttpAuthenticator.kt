package com.soundhub.utils.request_interceptors

import android.util.Log
import com.google.gson.Gson
import com.soundhub.data.api.AuthService
import com.soundhub.data.api.requests.RefreshTokenRequestBody
import com.soundhub.data.datastore.UserCredsStore
import com.soundhub.data.datastore.UserPreferences
import com.soundhub.ui.events.UiEvent
import com.soundhub.ui.viewmodels.UiStateDispatcher
import com.soundhub.utils.HttpUtils
import com.soundhub.utils.constants.Constants
import com.soundhub.Route as AppRoute
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.runBlocking
import okhttp3.Authenticator
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response as HttpResponse
import retrofit2.Response
import okhttp3.Route
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Inject

class HttpAuthenticator @Inject constructor(
    private val userCredsStore: UserCredsStore,
    private val uiStateDispatcher: UiStateDispatcher
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

    override fun authenticate(route: Route?, response: HttpResponse): Request? {
        val oldCreds: UserPreferences? = runBlocking { userCredsStore.getCreds().firstOrNull() }
        if (oldCreds?.refreshToken == null)
            return response.request

        val newCreds: UserPreferences? = runBlocking { refreshToken() }
        val request: Request = response.request

        newCreds?.let {
            val bearerToken: String = HttpUtils.getBearerToken(newCreds.accessToken)

            if (!request.headers.names().contains(HttpUtils.AUTHORIZATION_HEADER))
                return response.request
                    .newBuilder()
                    .addHeader(HttpUtils.AUTHORIZATION_HEADER, bearerToken)
                    .build()
        }

        return request
    }

    private suspend fun refreshToken(): UserPreferences? {
        val oldCreds: UserPreferences? = userCredsStore.getCreds().firstOrNull()
        val requestBody = RefreshTokenRequestBody(oldCreds?.refreshToken)
        val newCreds = UserPreferences()

        val refreshTokenResponse: Response<UserPreferences> = authService.refreshToken(requestBody)
        Log.d("HttpAuthenticator", "refreshToken[1]: response = $refreshTokenResponse")

        if (!refreshTokenResponse.isSuccessful) {
            val route = AppRoute.Authentication
            uiStateDispatcher.sendUiEvent(UiEvent.Navigate(route))
            userCredsStore.clear()
            return null
        }

        val body: UserPreferences? = refreshTokenResponse.body()

        with(newCreds) {
            accessToken = body?.accessToken
            refreshToken = body?.refreshToken
        }

        Log.d("HttpAuthenticator", "refreshToken[2]: creds = $newCreds")
        userCredsStore.updateCreds(newCreds)

        return newCreds
    }
}