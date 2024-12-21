package com.soundhub.utils.extensions.activity

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.ActivityCompat

fun Activity.requestNotificationPermissions() {
	if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
		ActivityCompat.checkSelfPermission(
			this,
			Manifest.permission.POST_NOTIFICATIONS
		) != PackageManager.PERMISSION_GRANTED
	) {
		ActivityCompat.requestPermissions(
			this,
			arrayOf(Manifest.permission.POST_NOTIFICATIONS),
			101
		)
	}
}