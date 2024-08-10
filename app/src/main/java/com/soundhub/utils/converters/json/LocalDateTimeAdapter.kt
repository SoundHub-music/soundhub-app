package com.soundhub.utils.converters.json

import android.util.Log
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonNull
import com.google.gson.JsonPrimitive
import com.google.gson.JsonSerializationContext
import com.google.gson.JsonSerializer
import com.soundhub.utils.constants.Constants.FALLBACK_LOCAL_DATETIME_PATTERN
import com.soundhub.utils.constants.Constants.LOCAL_DATETIME_PATTERN
import java.lang.reflect.Type
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class LocalDateTimeAdapter : JsonSerializer<LocalDateTime?>, JsonDeserializer<LocalDateTime?> {
	private val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern(LOCAL_DATETIME_PATTERN)
	private val fallbackFormatter = DateTimeFormatter.ofPattern(FALLBACK_LOCAL_DATETIME_PATTERN)

	override fun serialize(
		src: LocalDateTime?,
		typeOfSrc: Type?,
		context: JsonSerializationContext?
	): JsonElement? = if (src == null) JsonNull.INSTANCE
	else JsonPrimitive(src.format(formatter).toString())

	override fun deserialize(
		json: JsonElement?,
		typeOfT: Type?,
		context: JsonDeserializationContext?
	): LocalDateTime? = try {
		json?.asString?.let { LocalDateTime.parse(it, formatter) }
	} catch (e: Exception) {
		Log.d("LocalDateTimeAdapter", "deserialize[error]: ${e.stackTraceToString()}")
		json?.asString?.let { LocalDateTime.parse(it, fallbackFormatter) }
	}
}
