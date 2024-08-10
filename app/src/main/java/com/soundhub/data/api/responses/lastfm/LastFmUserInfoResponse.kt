package com.soundhub.data.api.responses.lastfm

import com.google.gson.annotations.SerializedName

data class LastFmUserInfoResponse(
	val user: LastFmFullUser
)

data class LastFmFullUser(
	val name: String,
	val age: Int,
	val subscriber: Int,

	@SerializedName("realname")
	val realName: String,

	@SerializedName("playcount")
	val playCount: Int,

	@SerializedName("playlists")
	val playlistCount: Int,

	@SerializedName("artist_count")
	val artistCount: Int,

	@SerializedName("track_count")
	val trackCount: Int,

	@SerializedName("album_count")
	val albumCount: Int,

	@SerializedName("image")
	val images: List<LastFmUserImage> = emptyList(),

	val registered: LastFmUserRegisterTime,

	val country: String,

	val gender: String,

	@SerializedName("url")
	val userLink: String
)

data class LastFmUserImage(
	val size: String,

	@SerializedName("#text")
	val text: String
)

data class LastFmUserRegisterTime(
	@SerializedName("unixtime")
	val unixTime: String
)