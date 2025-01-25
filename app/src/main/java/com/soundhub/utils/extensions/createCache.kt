package com.soundhub.utils.extensions

import android.content.Context
import com.soundhub.utils.constants.Constants.CACHE_SIZE
import okhttp3.Cache
import java.io.File

fun Cache.Companion.createCache(cacheSize: Long = CACHE_SIZE, context: Context): Cache {
	val cacheDirectory = File(context.cacheDir, "http-cache")

	return Cache(cacheDirectory, cacheSize)
}