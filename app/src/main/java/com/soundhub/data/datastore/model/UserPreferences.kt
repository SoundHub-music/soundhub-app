package com.soundhub.data.datastore.model

data class UserPreferences(
	var accessToken: String? = null,
	var refreshToken: String? = null
) {
	fun isValid() = !accessToken.isNullOrEmpty() && !refreshToken.isNullOrEmpty()
}