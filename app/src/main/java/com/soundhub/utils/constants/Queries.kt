package com.soundhub.utils.constants

object Queries {
	private const val TRUNCATE_COMMAND = "DELETE FROM"
	private const val GET_COMMAND = "SELECT * FROM"

	private const val USER_TABLE = "User"
	private const val LAST_FM_USER_TABLE = "LastFmUser"
	private const val COUNTRY_TABLE = "Country"
	private const val WALL_POST_TABLE = "Post"

	const val GET_USER = "$GET_COMMAND $USER_TABLE"
	const val GET_COUNTRIES = "$GET_COMMAND $COUNTRY_TABLE"
	const val TRUNCATE_USER = "$TRUNCATE_COMMAND $USER_TABLE"
	const val TRUNCATE_WALL_POSTS = "$TRUNCATE_COMMAND $WALL_POST_TABLE"

	const val GET_LASTFM_USER = "$GET_COMMAND $LAST_FM_USER_TABLE"
	const val TRUNCATE_LAST_FM_USER = "$TRUNCATE_COMMAND $LAST_FM_USER_TABLE"
	const val GET_WALL_POSTS = "$GET_COMMAND $WALL_POST_TABLE"
}