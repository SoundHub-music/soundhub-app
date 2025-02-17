package com.soundhub.utils.converters.room

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.soundhub.data.api.responses.lastfm.LastFmImage
import java.lang.reflect.Type

class LastFmImageConverter {
	private val gson: Gson
		get() = Gson()

	private val lastFmImageType: Type
		get() = object : TypeToken<LastFmImage>() {}.type

	private val imageListType: Type
		get() = object : TypeToken<List<LastFmImage>>() {}.type

	@TypeConverter
	fun toStringImage(obj: List<LastFmImage>): String {
		return gson.toJson(obj)
	}

	@TypeConverter
	fun toLastFmImageList(json: String): List<LastFmImage> {
		return gson.fromJson(json, imageListType)
	}

	@TypeConverter
	fun toLastFmImage(json: String): LastFmImage {
		return gson.fromJson(json, lastFmImageType)
	}

	@TypeConverter
	fun fromLastFmImageToString(obj: LastFmImage): String {
		return gson.toJson(obj)
	}
}