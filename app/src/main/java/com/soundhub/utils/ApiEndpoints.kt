package com.soundhub.utils

sealed class ApiEndpoints {
    object SoundHub {
        private const val authEndpoint: String = "auth"
        private const val usersEndpoint: String = "users"
        private const val userIdDynamicParam: String = "{userId}"

        const val signUp: String = "$authEndpoint/sign-up"
        const val signIn: String = "$authEndpoint/sign-in"
        const val logout: String = "$authEndpoint/logout"
        const val refreshToken: String = "$authEndpoint/refresh"

        const val currentUser: String = "$usersEndpoint/currentUser"
        const val getUserById: String = "$usersEndpoint/$userIdDynamicParam"
        const val updateUser: String = "$usersEndpoint/update/$userIdDynamicParam"
    }

    object Countries {
        const val allCountries: String = "all"
    }

    object Music {
        const val releaseIdDynamicParam = "releaseId"
        const val artistIdDynamicParam = "artistId"
        const val artistEndpoint: String = "artists"
        const val releaseEndpoint: String = "releases"

        const val databaseSearch: String = "database/search"

        const val artists: String = "$artistEndpoint/{$artistIdDynamicParam}"
        const val releases: String = "$releaseEndpoint/{$releaseIdDynamicParam}"
        const val artistReleases = "$artists/releases"
    }

    object Files {
        const val filesEndpoint: String = "files"
        const val getFile: String = "$filesEndpoint/{filename}"
    }
}