package com.soundhub.utils.constants

object Queries {
	private const val USER_TABLE = "User"
	private const val LAST_FM_USER_TABLE = "LastFmUser"
	private const val COUNTRY_TABLE = "Country"
	private const val WALL_POST_TABLE = "Post"

	const val GET_USER = "SELECT * FROM $USER_TABLE"
	const val GET_COUNTRIES = "SELECT * FROM $COUNTRY_TABLE"
	const val TRUNCATE_USER = "DELETE FROM $USER_TABLE"
	const val TRUNCATE_WALL_POSTS = "DELETE FROM $WALL_POST_TABLE"

	const val GET_LASTFM_USER = "SELECT * FROM $LAST_FM_USER_TABLE"
	const val GET_WALL_POSTS = "SELECT * FROM $WALL_POST_TABLE"
}