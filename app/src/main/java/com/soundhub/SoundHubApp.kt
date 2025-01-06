package com.soundhub

import android.app.Application
import coil3.ImageLoader
import coil3.PlatformContext
import coil3.SingletonImageLoader
import coil3.memory.MemoryCache
import coil3.request.crossfade
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class SoundHubApp() : Application(), SingletonImageLoader.Factory {
	override fun newImageLoader(context: PlatformContext): ImageLoader {
		return ImageLoader.Builder(context)
			.crossfade(true)
			.memoryCache {
				MemoryCache.Builder()
					.maxSizePercent(context, 0.25)
					.build()
			}
			.build()
	}
}