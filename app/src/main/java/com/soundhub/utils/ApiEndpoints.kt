package com.soundhub.utils

sealed class ApiEndpoints {
    object SoundHub {
        const val authEndpoint: String = "auth"
        const val usersEndpoint: String = "users"
        const val userIdDynamicParam: String = "{userId}"

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
        const val genreEndpoint: String = "genre"
        const val artistEndpoint: String = "artist"

        const val allGenres: String = "$genreEndpoint/all"
    }

    object Files {
        const val filesEndpoint: String = "files"
        const val getFile: String = "$filesEndpoint/{filename}"
    }
}