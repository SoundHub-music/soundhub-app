package com.soundhub.utils.interceptors

import com.soundhub.data.datastore.UserCredsStore
import com.soundhub.data.datastore.model.UserPreferences
import com.soundhub.utils.extensions.request.withAppAuthorization
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response as HttpResponse

class AuthInterceptor(private val userCredsStore: UserCredsStore) : Interceptor {
	override fun intercept(chain: Interceptor.Chain): HttpResponse {
		val tokens: UserPreferences? = runBlocking { userCredsStore.getCreds().firstOrNull() }
		val request: Request = chain.request().withAppAuthorization(tokens)

		return chain.proceed(request)
	}
}