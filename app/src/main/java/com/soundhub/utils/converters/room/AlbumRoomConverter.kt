package com.soundhub.utils.converters.room

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import com.soundhub.domain.model.Album
import com.soundhub.utils.converters.json.LocalDateAdapter
import java.lang.reflect.Type
import java.time.LocalDate

class AlbumRoomConverter {
	private val gson: Gson
		get() = GsonBuilder()
			.registerTypeAdapter(LocalDate::class.java, LocalDateAdapter::class.java)
			.create()

	private val albumListType: Type
		get() = object : TypeToken<List<Album>>() {}.type

	private val albumType: Type
		get() = object : TypeToken<Album>() {}.type

	@TypeConverter
	fun toStringAlbum(albumList: Album): String {
		return gson.toJson(albumList)
	}

	@TypeConverter
	fun fromStringAlbum(albumJson: String): Album {
		return gson.fromJson(albumJson, albumType)
	}

	@TypeConverter
	fun toStringAlbumList(albumList: List<Album>): String {
		return gson.toJson(albumList)
	}

	@TypeConverter
	fun fromStringAlbumList(albumJson: String): List<Album> {
		return gson.fromJson(albumJson, albumListType)
	}

}