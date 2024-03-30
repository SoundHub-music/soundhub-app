package com.soundhub.utils

import com.google.gson.reflect.TypeToken
import com.soundhub.BuildConfig
import com.soundhub.Route
import com.soundhub.data.api.responses.ErrorResponse
import java.lang.reflect.Type

object Constants {
    const val PASSWORD_MIN_LENGTH: Int = 8
    const val EMAIL_REGEX: String = "^[A-Za-z](.*)(@)(.+)(\\.)(.+)"
    const val NAV_ARG_REGEX: String = """\{[^}]*\}"""

    // datastore identifiers
    const val DATASTORE_USER_CREDS = "user_creds"
    const val DATASTORE_ACCESS_TOKEN = "user_access_token"
    const val DATASTORE_REFRESH_TOKEN = "user_refresh_token"

    const val LOG_CURRENT_EVENT_TAG = "current_event"

    const val DATE_FORMAT = "yyyy-MM-dd"

    val ROUTES_WITH_CUSTOM_TOP_APP_BAR: List<String> = listOf(
        Route.Postline.route,
        Route.Music.route,
        Route.Messenger.route
    )

    val ROUTES_WITHOUT_TOP_APP_BAR: List<String> = listOf(
        Route.Authentication.withNavArg,
        Route.Authentication.route,
        Route.Profile().route
    )

    val ROUTES_WITH_BOTTOM_BAR: List<String> = listOf(
        Route.Profile().route,
        Route.Postline.route,
        Route.Music.route,
        Route.Messenger.route
    )


    // nav arguments
    const val POST_REGISTER_NAV_ARG = "postAuthId"
    const val PROFILE_NAV_ARG = "userId"
    const val CHAT_NAV_ARG = "chatId"
    const val GALLERY_INITIAL_PAGE_NAV_ARG = "imageIndex"

    // routes
    // authentication routes
    const val AUTHENTICATION_ROUTE = "authentication"
    const val CHOOSE_GENRES_ROUTE = "$AUTHENTICATION_ROUTE/chooseGenres"
    const val CHOOSE_ARTISTS_ROUTE = "$AUTHENTICATION_ROUTE/chooseArtists"
    const val FILL_DATA_REGISTRATION_ROUTE = "$AUTHENTICATION_ROUTE/fillDataRegister"


    const val PROFILE_ROUTE = "profile/{$PROFILE_NAV_ARG}"
    const val POSTLINE_ROUTE = "postline"

    const val MUSIC_ROUTE = "music"

    // messenger routes
    const val MESSENGER_ROUTE = "messenger"
    const val MESSENGER_CHAT_ROUTE = "$MESSENGER_ROUTE/chat/{$CHAT_NAV_ARG}"

    const val SETTINGS_ROUTE = "settings"
    const val NOTIFICATIONS_ROUTE = "notifications"
    const val EDIT_USER_DATA_ROUTE = "edit-data"
    const val CREATE_POST_ROUTE = "create-post"
    const val GALLERY_ROUTE = "gallery"
    const val FRIEND_LIST_ROUTE = "friends"

    const val MUSIC_NEW_OF_THE_WEEK = "new-of-the-week"
    const val MUSIC_NEW_OF_THE_MONTH = "new-of-the-month"
    const val MUSIC_RECOMMENDATIONS = "recommend-music"

    // response error body type
    val ERROR_BODY_TYPE: Type = object : TypeToken<ErrorResponse>(){}.type

    // named injections for di
    const val SOUNDHUB_API_RETROFIT = "soundhub_api"
    const val MUSIC_API_RETROFIT = "music_api"
    const val COUNTRIES_API_RETROFIT = "countries_api"
    const val LAST_FM_API_RETROFIT = "lastfm_api"


    const val DISCOGS_AUTHORIZATION = "Discogs key=${BuildConfig.DISCOGS_KEY}, secret=${BuildConfig.DISCOGS_SECRET}"

}