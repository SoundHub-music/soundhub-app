package com.soundhub.utils.converters.json

import com.google.gson.Gson
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonSerializationContext
import com.google.gson.JsonSerializer
import com.google.gson.reflect.TypeToken
import com.soundhub.utils.constants.Constants
import java.lang.reflect.Type
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class LocalDateWebSocketAdapter : JsonSerializer<LocalDate?>, JsonDeserializer<LocalDate?> {
	private val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern(Constants.DATE_FORMAT)

	override fun serialize(
		src: LocalDate?,
		typeOfSrc: Type?,
		context: JsonSerializationContext?
	): JsonElement? = src?.format(formatter)?.let { context?.serialize(it) }

	override fun deserialize(
		json: JsonElement?,
		typeOfT: Type?,
		context: JsonDeserializationContext?
	): LocalDate? {
		val type: Type = object : TypeToken<List<Int>>() {}.type
		val dateArray: List<Int> = Gson().fromJson(json, type)
		if (dateArray.size == 3)
			return LocalDate.of(dateArray.first(), dateArray[1], dateArray.last())
		return null
	}
}