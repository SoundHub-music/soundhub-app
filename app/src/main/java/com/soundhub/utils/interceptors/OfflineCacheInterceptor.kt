package com.soundhub.utils.interceptors

import android.util.Log
import okhttp3.CacheControl
import okhttp3.Interceptor
import okhttp3.Response
import java.util.concurrent.TimeUnit

class OfflineCacheInterceptor : Interceptor {
	override fun intercept(chain: Interceptor.Chain): Response {
		try {
			return chain.proceed(chain.request())
		} catch (e: Exception) {
			Log.e("OfflineCacheInterceptor", "intercept[1]: ${e.stackTraceToString()}")
			val cacheControl = CacheControl.Builder()
				.onlyIfCached()
				.maxStale(7, TimeUnit.DAYS)
				.build()

			val offlineRequest = chain.request().newBuilder()
				.cacheControl(cacheControl)
				.build()

			return chain.proceed(offlineRequest)
		}
	}

}