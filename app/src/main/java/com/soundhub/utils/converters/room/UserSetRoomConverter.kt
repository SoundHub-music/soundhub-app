package com.soundhub.utils.converters.room

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import com.soundhub.data.model.User
import com.soundhub.utils.converters.json.LocalDateAdapter
import com.soundhub.utils.converters.json.LocalDateTimeAdapter
import java.lang.reflect.Type
import java.time.LocalDate
import java.time.LocalDateTime

class UserSetRoomConverter {
	private val gson: Gson
		get() = GsonBuilder()
			.registerTypeAdapter(LocalDate::class.java, LocalDateAdapter())
			.registerTypeAdapter(LocalDateTime::class.java, LocalDateTimeAdapter())
			.create()

	private val userSetType: Type
		get() = object : TypeToken<Set<User>>() {}.type

	@TypeConverter
	fun toStringUserSet(userList: Set<User>): String {
		return gson.toJson(userList)
	}

	@TypeConverter
	fun fromStringUserSet(userJson: String): Set<User> {
		return gson.fromJson(userJson, userSetType)
	}
}