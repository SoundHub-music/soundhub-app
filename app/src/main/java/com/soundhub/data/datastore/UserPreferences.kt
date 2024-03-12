package com.soundhub.data.datastore

data class UserPreferences(
    val accessToken: String? = null,
    val refreshToken: String? = null
)