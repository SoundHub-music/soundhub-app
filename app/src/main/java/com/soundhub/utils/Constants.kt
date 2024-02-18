package com.soundhub.utils

import com.soundhub.Route

object Constants {
    const val PASSWORD_MIN_LENGTH: Int = 6
    const val EMAIL_REGEX: String = "^[A-Za-z](.*)(@)(.+)(\\.)(.+)"
    const val NAV_ARG_REGEX: String = """\{[^}]*\}"""

    const val DB_USERS: String = "users"

    // datastore identifiers
    const val DATASTORE_USER_CREDS = "user_creds"
    const val DATASTORE_USER_FIRST_NAME = "user_first_name"
    const val DATASTORE_USER_LASTNAME = "user_last_name"
    const val DATASTORE_USER_ID = "user_id"
    const val DATASTORE_USER_COUNTRY = "user_country"
    const val DATASTORE_USER_CITY = "user_city"
    const val DATASTORE_USER_EMAIL = "user_email"
    const val DATASTORE_USER_DESCRIPTION = "user_description"
    /* TODO: make datastore saving logic for user languages */
    const val DATASTORE_USER_LANGUAGES = "user_languages"
    const val DATASTORE_SESSION_TOKEN = "user_session_token"

    const val LOG_CURRENT_EVENT_TAG = "current_event"
    const val LOG_USER_CREDS_TAG = "user_creds"
    const val LOG_REGISTER_STATE = "registration_state"
    const val LOG_CURRENT_ROUTE = "current_route"

    const val DATE_FORMAT = "yyyy-MM-dd"

    val ROUTES_WITH_CUSTOM_TOP_APP_BAR: List<String> = listOf(
        Route.Postline.route,
        Route.Music.route,
        Route.Messenger.route
    )

    val ROUTES_WITHOUT_TOP_APP_BAR: List<String> = listOf(
        Route.Authentication.route,
        Route.Authentication.ChooseGenres.route,
        Route.Authentication.ChooseArtists.route,
        Route.Authentication.FillUserData.route,
        Route.Profile().route
    )

    val ROUTES_WITH_BOTTOM_BAR: List<String> = listOf(
        Route.Profile().route,
        Route.Postline.route,
        Route.Music.route,
        Route.Messenger.route
    )

    // routes
    const val AUTHENTICATION_ROUTE = "authentication"
    const val CHOOSE_GENRES_ROUTE = "$AUTHENTICATION_ROUTE/chooseGenres"
    const val CHOOSE_ARTISTS_ROUTE = "$AUTHENTICATION_ROUTE/chooseArtists"
    const val FILL_DATA_REGISTRATION_ROUTE = "$AUTHENTICATION_ROUTE/fillDataRegister"

    const val POST_REGISTER_NAV_ARG = "postAuthId"
    const val PROFILE_NAV_ARG = "userId"
    const val CHAT_NAV_ARG = "chatId"
    const val GALLERY_NAV_ARG = "imageIndex"

    const val PROFILE_ROUTE = "profile/{$PROFILE_NAV_ARG}"
    const val POSTLINE_ROUTE = "postline"
    const val MUSIC_ROUTE = "music"
    const val MESSENGER_ROUTE = "messenger"
    const val MESSENGER_CHAT_ROUTE = "$MESSENGER_ROUTE/chat/{$CHAT_NAV_ARG}"
    const val SETTINGS_ROUTE = "settings"
    const val NOTIFICATIONS_ROUTE = "notifications"
    const val EDIT_USER_DATA_ROUTE = "edit-data"
    const val CREATE_POST_ROUTE = "create-post"
    const val GALLERY_ROUTE = "gallery"
    const val FRIEND_LIST_ROUTE = "friends"
}