package com.soundhub.utils.extensions.intent

import android.content.Intent
import android.os.Build
import java.io.Serializable

@Suppress("UNCHECKED_CAST", "DEPRECATION")
fun <T : Serializable> Intent.getSerializableExtraExtended(key: String, clazz: Class<T>): T? =
	when {
		Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU -> getSerializableExtra(key, clazz)
		else -> getSerializableExtra(key) as? T
	}