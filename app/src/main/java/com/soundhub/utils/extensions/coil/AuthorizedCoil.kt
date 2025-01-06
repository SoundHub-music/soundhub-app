package com.soundhub.utils.extensions.coil

import coil3.network.okhttp.OkHttpNetworkFetcherFactory
import coil3.request.ImageRequest
import com.soundhub.data.datastore.model.UserPreferences
import com.soundhub.utils.extensions.request.withAppAuthorization
import okhttp3.OkHttpClient

fun ImageRequest.Builder.withAccessToken(userCreds: UserPreferences?): ImageRequest.Builder {
	val okHttpClient = OkHttpClient.Builder()
		.addInterceptor { chain ->
			val request = chain.request().withAppAuthorization(userCreds)
			chain.proceed(request)
		}
		.build()

	this.fetcherFactory(factory = OkHttpNetworkFetcherFactory(callFactory = { okHttpClient }))

	return this
}