package com.soundhub.utils.request_interceptors

import com.soundhub.data.datastore.UserCredsStore
import com.soundhub.data.datastore.UserPreferences
import com.soundhub.utils.HttpUtils
import com.soundhub.utils.HttpUtils.Companion.AUTHORIZATION_HEADER
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response as HttpResponse

class AuthInterceptor(
    private val userCredsStore: UserCredsStore,
): Interceptor {
    override fun intercept(chain: Interceptor.Chain): HttpResponse {
        val tokens: UserPreferences? = runBlocking { userCredsStore.getCreds().firstOrNull() }
        val bearerToken: String = HttpUtils.getBearerToken(tokens?.accessToken)

        val request = chain.request()
            .newBuilder()
            .addHeader(AUTHORIZATION_HEADER, bearerToken)
            .build()

        return chain.proceed(request)
    }
}