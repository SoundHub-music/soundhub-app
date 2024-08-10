package com.soundhub.utils.converters.json

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonNull
import com.google.gson.JsonPrimitive
import com.google.gson.JsonSerializationContext
import com.google.gson.JsonSerializer
import com.soundhub.utils.constants.Constants.DATE_FORMAT
import java.lang.reflect.Type
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class LocalDateAdapter : JsonSerializer<LocalDate?>, JsonDeserializer<LocalDate?> {
	private val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern(DATE_FORMAT)

	override fun serialize(
		src: LocalDate?,
		typeOfSrc: Type?,
		context: JsonSerializationContext?
	): JsonElement {
		return if (src == null) JsonNull.INSTANCE else JsonPrimitive(
			src.format(formatter).toString()
		)
	}

	override fun deserialize(
		json: JsonElement?,
		typeOfT: Type?,
		context: JsonDeserializationContext?
	): LocalDate? {
		return if (json == null || json !is JsonPrimitive || !json.isString) {
			null
		} else {
			LocalDate.parse(json.asString, formatter)
		}
	}
}
