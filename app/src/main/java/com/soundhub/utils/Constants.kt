package com.soundhub.utils

object Constants {
    const val PASSWORD_MIN_LENGTH: Int = 6
    const val EMAIL_MASK: String = "^[A-Za-z](.*)(@)(.+)(\\.)(.+)"

    const val DB_USERS: String = "users"

    const val DATASTORE_USER_CREDS = "user_creds"
    const val DATASTORE_USER_FIRST_NAME = "user_first_name"
    const val DATASTORE_USER_LASTNAME = "user_last_name"
    const val DATASTORE_USER_ID = "user_id"
    const val DATASTORE_USER_COUNTRY = "user_country"
    const val DATASTORE_USER_CITY = "user_city"
    const val DATASTORE_USER_EMAIL = "user_email"
    const val DATASTORE_USER_DESCRIPTION = "user_description"

    // TODO: make datastore saving logic for user languages
    const val DATASTORE_USER_LANGUAGES = "user_languages"
    const val DATASTORE_SESSION_TOKEN = "user_session_token"


    const val LOG_CURRENT_EVENT_TAG = "current_event"
    const val LOG_USER_CREDS_TAG = "user_creds"
}