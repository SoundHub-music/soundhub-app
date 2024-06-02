package com.soundhub.data.datastore

data class UserPreferences(
    var accessToken: String? = null,
    var refreshToken: String? = null
)