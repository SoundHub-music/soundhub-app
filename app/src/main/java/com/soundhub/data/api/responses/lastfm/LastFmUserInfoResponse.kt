package com.soundhub.data.api.responses.lastfm

import com.google.gson.annotations.SerializedName

data class LastFmUserInfoResponse(
	@SerializedName("user")
	val user: LastFmFullUser
)

data class LastFmFullUser(
	val name: String,
	val age: String,
	val subscriber: String,

	@SerializedName("realname")
	val realName: String,

	@SerializedName("playcount")
	val playCount: String,

	@SerializedName("playlists")
	val playlistCount: String,

	@SerializedName("artist_count")
	val artistCount: String,

	@SerializedName("track_count")
	val trackCount: String,

	@SerializedName("album_count")
	val albumCount: String,

	@SerializedName("image")
	val images: List<LastFmUserImage>,

	val registered: LastFmUserRegisterTime,

	val country: String,
	val gender: String,

	@SerializedName("url")
	val userLink: String,

	val type: String
)

data class LastFmUserImage(
	val size: String,

	@SerializedName("#text")
	val url: String
)

data class LastFmUserRegisterTime(
	@SerializedName("unixtime")
	val unixTime: String,

	@SerializedName("#text")
	val text: String
)
