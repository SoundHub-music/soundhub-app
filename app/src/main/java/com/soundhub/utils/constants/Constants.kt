package com.soundhub.utils.constants

import com.google.gson.reflect.TypeToken
import com.soundhub.BuildConfig
import com.soundhub.Route
import com.soundhub.data.api.responses.ErrorResponse
import java.lang.reflect.Type

object Constants {
    const val PASSWORD_MIN_LENGTH: Int = 8
    const val DATE_FORMAT = "yyyy-MM-dd"
    const val DATETIME_FORMAT = "yyyy-MM-dd HH:mm:ss"

    // online status
    const val SET_OFFLINE_DELAY_ON_DESTROY: Long = 60 * 1000
    const val SET_OFFLINE_DELAY_ON_STOP: Long = 5 * 60 * 1000

    // regular expressions
    const val EMAIL_REGEX: String = "^[A-Za-z](.*)(@)(.+)(\\.)(.+)"
    const val DYNAMIC_PARAM_REGEX: String = """\{[^}]*\}"""

    // datastore identifiers
    const val DATASTORE_USER_CREDS = "user_creds"
    const val DATASTORE_ACCESS_TOKEN = "user_access_token"
    const val DATASTORE_REFRESH_TOKEN = "user_refresh_token"

    // room db
    const val ROOM_DB_NAME = "soundhub_db"

    // route list constants
    val ROUTES_WITH_CUSTOM_TOP_APP_BAR: List<String> = listOf(
        Route.Postline.route,
        Route.Music.route,
        Route.Messenger.route
    )

    val ROUTES_WITHOUT_TOP_APP_BAR: List<String> = listOf(
        Route.Authentication.withNavArg,
        Route.Authentication.route,
        Route.Profile.route,
        Route.Messenger.Chat.route,
        Route.EditFavoriteArtists.route,
        Route.EditFavoriteGenres.route
    )

    val ROUTES_WITH_BOTTOM_BAR: List<String> = listOf(
        Route.Profile.route,
        Route.Postline.route,
        Route.Music.route,
        Route.Messenger.route
    )

    val ROUTES_WITH_SEARCH_BAR: List<String> = listOf(
        Route.Music.route,
        Route.Messenger.route,
        Route.Profile.Friends.route
    )

    // nav arguments
    const val POST_REGISTER_NAV_ARG = "postAuthId"
    const val PROFILE_NAV_ARG = "userId"
    const val CHAT_NAV_ARG = "chatId"
    const val GALLERY_INITIAL_PAGE_NAV_ARG = "imageIndex"
    const val POST_EDITOR_NAV_ARG = "postId"

    val ALL_NAV_ARGS: List<String> = listOf(
        POST_REGISTER_NAV_ARG,
        PROFILE_NAV_ARG,
        CHAT_NAV_ARG,
        GALLERY_INITIAL_PAGE_NAV_ARG,
        POST_EDITOR_NAV_ARG
    )

    // routes
    // authentication routes
    const val AUTHENTICATION_ROUTE = "authentication"
    const val CHOOSE_GENRES_ROUTE = "$AUTHENTICATION_ROUTE/chooseGenres"
    const val CHOOSE_ARTISTS_ROUTE = "$AUTHENTICATION_ROUTE/chooseArtists"
    const val FILL_DATA_REGISTRATION_ROUTE = "$AUTHENTICATION_ROUTE/fillDataRegister"

    const val PROFILE_ROUTE = "profile/{$PROFILE_NAV_ARG}"
    const val FRIENDS_ROUTE = "${PROFILE_ROUTE}/friends"
    const val POSTLINE_ROUTE = "postline"
    const val MUSIC_ROUTE = "music"

    // messenger routes
    const val MESSENGER_ROUTE = "messenger"
    const val MESSENGER_CHAT_ROUTE = "$MESSENGER_ROUTE/chat/{$CHAT_NAV_ARG}"

    const val SETTINGS_ROUTE = "settings"
    const val NOTIFICATIONS_ROUTE = "notifications"
    const val EDIT_USER_DATA_ROUTE = "user-editor"
    const val EDIT_FAV_GENRES_ROUTE = "fav-genres-editor"
    const val EDIT_FAV_ARTISTS_ROUTE = "fav-artists-editor"
    const val POST_EDITOR_ROUTE = "post-editor/{$POST_EDITOR_NAV_ARG}"
    const val GALLERY_ROUTE = "gallery"

    // music page
    const val MUSIC_NEW_OF_THE_WEEK = "new-of-the-week"
    const val MUSIC_NEW_OF_THE_MONTH = "new-of-the-month"
    const val MUSIC_RECOMMENDATIONS = "recommend-music"

    // response error body type
    val ERROR_BODY_TYPE: Type = object : TypeToken<ErrorResponse>(){}.type

    // named hilt injections
    const val COUNTRIES_API_RETROFIT = "countries_api"
    const val SOUNDHUB_API_RETROFIT = "soundhub_api"
    const val LAST_FM_API_RETROFIT = "lastfm_api"
    const val MUSIC_API_RETROFIT = "music_api"

    // API constants
    const val COUNTRIES_API = "https://restcountries.com/v3.1/"
    const val SOUNDHUB_API_HOSTNAME="192.168.1.41:8080"
    const val SOUNDHUB_API = "http://$SOUNDHUB_API_HOSTNAME/api/v1/"
    const val SOUNDHUB_WEBSOCKET = "ws://$SOUNDHUB_API_HOSTNAME/ws"
    const val LAST_FM_API = "https://ws.audioscrobbler.com/"
    const val DISCOGS_API = "https://api.discogs.com/"

    // Discogs API authorization header
    const val DISCOGS_AUTHORIZATION = "Discogs key=${BuildConfig.DISCOGS_KEY}, secret=${BuildConfig.DISCOGS_SECRET}"
}