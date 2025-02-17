package com.soundhub.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.google.gson.annotations.SerializedName
import com.soundhub.data.api.responses.lastfm.LastFmImage
import com.soundhub.utils.converters.room.LastFmImageConverter
import com.soundhub.utils.converters.room.LastFmRegisterTimeConverter

data class LastFmUserDto(
	val name: String,

	@SerializedName("key")
	val sessionKey: String,
	val subscriber: Int
)

@Entity
@TypeConverters(
	LastFmImageConverter::class,
	LastFmRegisterTimeConverter::class
)
data class LastFmUser(
	@PrimaryKey
	val name: String,
	val age: String?,
	val subscriber: String,

	@SerializedName("key")
	val sessionKey: String,

	@SerializedName("realname")
	val realName: String?,

	@SerializedName("playcount")
	val playCount: String?,

	@SerializedName("playlists")
	val playlistCount: String?,

	@SerializedName("artist_count")
	val artistCount: String?,

	@SerializedName("track_count")
	val trackCount: String?,

	@SerializedName("album_count")
	val albumCount: String?,

	@SerializedName("image")
	val images: List<LastFmImage>? = emptyList(),

	val registered: LastFmUserRegisterTime?,

	val country: String?,
	val gender: String?,

	@SerializedName("url")
	val url: String?,

	val type: String?
)

data class LastFmUserRegisterTime(
	@SerializedName("unixtime")
	val unixTime: String,

	@SerializedName("#text")
	val text: String
)
