package com.soundhub.utils.converters.room

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import com.soundhub.domain.model.Artist
import com.soundhub.utils.converters.json.LocalDateAdapter
import java.lang.reflect.Type
import java.time.LocalDate

class ArtistRoomConverter {
	private val gson: Gson
		get() = GsonBuilder()
			.registerTypeAdapter(LocalDate::class.java, LocalDateAdapter())
			.create()

	private val artistListType: Type
		get() = object : TypeToken<List<Artist>>() {}.type

	@TypeConverter
	fun toStringArtist(artistList: Artist): String {
		return gson.toJson(artistList)
	}

	@TypeConverter
	fun fromStringArtist(artistJson: String): Artist {
		return gson.fromJson(artistJson, artistListType)
	}

	@TypeConverter
	fun toStringArtistList(artistList: List<Artist>): String {
		return gson.toJson(artistList)
	}

	@TypeConverter
	fun fromStringArtistList(artistJson: String): List<Artist> {
		return gson.fromJson(artistJson, artistListType)
	}
}