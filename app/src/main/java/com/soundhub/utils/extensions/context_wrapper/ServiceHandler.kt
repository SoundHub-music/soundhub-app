package com.soundhub.utils.extensions.context_wrapper

import android.content.ContextWrapper
import android.content.Intent

fun <K> ContextWrapper.startAndroidService(clazz: Class<K>) {
	val intent = Intent(this, clazz)
	startService(intent)
}

fun <K> ContextWrapper.stopAndroidService(clazz: Class<K>) {
	val intent = Intent(this, clazz)
	stopService(intent)
}