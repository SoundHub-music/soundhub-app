package com.soundhub.utils.extensions.request

import com.soundhub.data.datastore.model.UserPreferences
import com.soundhub.utils.lib.HttpUtils
import com.soundhub.utils.lib.HttpUtils.Companion.AUTHORIZATION_HEADER
import okhttp3.Request

fun Request.withAppAuthorization(userCreds: UserPreferences?): Request {
	return withAppAuthorization(userCreds?.accessToken)
}

fun Request.withAppAuthorization(bearerToken: String?): Request {
	val headerNames: Set<String> = headers.names()
	val hasAuthorizationHeader: Boolean = headerNames.contains(AUTHORIZATION_HEADER)
	val hasAccessToken: Boolean = bearerToken != null

	if (hasAuthorizationHeader || !hasAccessToken)
		return this

	val bearerToken: String = HttpUtils.getBearerToken(bearerToken)

	return this.newBuilder()
		.addHeader(AUTHORIZATION_HEADER, bearerToken)
		.build()
}