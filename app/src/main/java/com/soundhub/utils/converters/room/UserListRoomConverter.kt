package com.soundhub.utils.converters.room

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import com.soundhub.domain.model.User
import com.soundhub.utils.converters.json.LocalDateAdapter
import com.soundhub.utils.converters.json.LocalDateTimeAdapter
import java.lang.reflect.Type
import java.time.LocalDate
import java.time.LocalDateTime

class UserListRoomConverter {
	private val gson: Gson
		get() = GsonBuilder()
			.registerTypeAdapter(LocalDate::class.java, LocalDateAdapter())
			.registerTypeAdapter(LocalDateTime::class.java, LocalDateTimeAdapter())
			.create()

	private val userListType: Type
		get() = object : TypeToken<List<User>>() {}.type

	@TypeConverter
	fun toStringUserList(userList: List<User>): String {
		return gson.toJson(userList)
	}

	@TypeConverter
	fun fromStringUserList(userJson: String): List<User> {
		return gson.fromJson(userJson, userListType)
	}
}