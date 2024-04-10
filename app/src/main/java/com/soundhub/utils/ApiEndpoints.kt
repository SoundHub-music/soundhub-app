package com.soundhub.utils

sealed class ApiEndpoints {
    object SoundHub {
        private const val AUTH_ENDPOINT: String = "auth"
        private const val USERS_ENDPOINT: String = "users"
        private const val USER_ID_DYNAMIC_PARAM: String = "{userId}"

        const val SIGN_UP: String = "$AUTH_ENDPOINT/sign-up"
        const val SIGN_IN: String = "$AUTH_ENDPOINT/sign-in"
        const val LOGOUT: String = "$AUTH_ENDPOINT/logout"
        const val REFRESH_TOKEN: String = "$AUTH_ENDPOINT/refresh"

        const val CURRENT_USER: String = "$USERS_ENDPOINT/currentUser"
        const val GET_USER_BY_ID: String = "$USERS_ENDPOINT/$USER_ID_DYNAMIC_PARAM"
        const val UPDATE_USER: String = "$USERS_ENDPOINT/update/$USER_ID_DYNAMIC_PARAM"
    }

    object Countries {
        const val ALL_COUNTRIES: String = "all"
    }

    object Music {
        const val RELEASE_ID_DYNAMIC_PARAM = "releaseId"
        const val ARTIST_ID_DYNAMIC_PARAM = "artistId"
        private const val ARTIST_ENDPOINT: String = "artists"
        private const val RELEASE_ENDPOINT: String = "releases"

        const val DATABASE_SEARCH: String = "database/search"

        const val ARTISTS: String = "$ARTIST_ENDPOINT/{$ARTIST_ID_DYNAMIC_PARAM}"
        const val RELEASES: String = "$RELEASE_ENDPOINT/{$RELEASE_ID_DYNAMIC_PARAM}"
        const val ARTIST_RELEASES = "$ARTISTS/releases"
    }

    object Files {
        private const val FILES_ENDPOINT: String = "files"
        const val GET_FILE: String = "$FILES_ENDPOINT/{filename}"
    }

    object Posts {
        private const val POSTS_ENDPOINT = "posts"
        const val POST_ID_DYNAMIC_PARAM = "postId"
        const val AUTHOR_ID_DYNAMIC_PARAM = "authorId"

        const val GET_POST_BY_ID = "$POSTS_ENDPOINT/{$POST_ID_DYNAMIC_PARAM}"
        const val GET_POSTS_BY_AUTHOR_ID = "$POSTS_ENDPOINT/post/{$AUTHOR_ID_DYNAMIC_PARAM}"
        const val ADD_POST = "$POSTS_ENDPOINT/add"
        const val TOGGLE_LIKE = "$POSTS_ENDPOINT/like/{$POST_ID_DYNAMIC_PARAM}"
        const val DELETE = "$POSTS_ENDPOINT/delete/{$POST_ID_DYNAMIC_PARAM}"
        const val UPDATE = "$POSTS_ENDPOINT/update/{$POST_ID_DYNAMIC_PARAM}"
    }

    object Chats {
        private const val CHATS_ENDPOINT = "chats"
        const val CHAT_ID_DYNAMIC_PARAM = "chatId"

        const val GET_CHATS_BY_CURRENT_USER = "$CHATS_ENDPOINT/user"
        const val GET_CHAT_BY_ID = "$CHATS_ENDPOINT/{$CHAT_ID_DYNAMIC_PARAM}"
        const val DELETE_CHAT = "$CHATS_ENDPOINT/delete/{$CHATS_ENDPOINT}"
        const val CREATE_CHAT = "$CHATS_ENDPOINT/single"
    }
}