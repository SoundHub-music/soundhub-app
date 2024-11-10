package com.soundhub.utils.helpers

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.ActivityCompat

fun requestNotificationPermissions(activity: Activity) {
	if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
		ActivityCompat.checkSelfPermission(
			activity,
			Manifest.permission.POST_NOTIFICATIONS
		) != PackageManager.PERMISSION_GRANTED
	) {
		ActivityCompat.requestPermissions(
			activity,
			arrayOf(Manifest.permission.POST_NOTIFICATIONS),
			101
		)
	}
}