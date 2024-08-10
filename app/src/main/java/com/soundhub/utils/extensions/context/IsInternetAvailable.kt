package com.soundhub.utils.extensions.context

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities

fun Context.isInternetAvailable(): Boolean {
	val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
	val activeNetwork = connectivityManager.activeNetwork ?: return false
	val networkCapabilities =
		connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false
	return networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
}