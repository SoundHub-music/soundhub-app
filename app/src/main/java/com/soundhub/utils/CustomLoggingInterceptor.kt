package com.soundhub.utils

import android.util.Log
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response

class CustomLoggingInterceptor: Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request: Request = chain.request()
        return try {
            Log.d("CustomLoggingInterceptor","Intercepted headers: ${request.headers} from URL: ${request.url}")
            chain.proceed(request)
        } catch (e: Exception) {
            Log.e("CustomLoggingInterceptor", "${e.message}")
            chain.proceed(request)
        }
    }
}