package com.soundhub.utils.converters.room

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.soundhub.domain.model.LastFmUserRegisterTime
import java.lang.reflect.Type

class LastFmRegisterTimeConverter {
	private val gson: Gson
		get() = Gson()

	private val lastFmUserRegisterTimeType: Type
		get() = object : TypeToken<LastFmUserRegisterTime>() {}.type

	@TypeConverter
	fun toStringRegisterTime(obj: LastFmUserRegisterTime): String {
		return gson.toJson(obj)
	}

	@TypeConverter
	fun fromStringRegisterTime(json: String): LastFmUserRegisterTime {
		return gson.fromJson(json, lastFmUserRegisterTimeType)
	}
}