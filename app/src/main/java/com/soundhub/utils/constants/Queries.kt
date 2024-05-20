package com.soundhub.utils.constants

object Queries {
    private const val USER_TABLE = "user"
    private const val COUNTRY_TABLE = "country"

    const val GET_USER = "SELECT * FROM $USER_TABLE"
    const val GET_COUNTRIES = "SELECT * FROM $COUNTRY_TABLE"
    const val TRUNCATE_USER = "DELETE FROM $USER_TABLE"
}