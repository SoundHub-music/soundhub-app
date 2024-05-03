package com.soundhub.utils.converters.json

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonPrimitive
import com.google.gson.JsonSerializationContext
import com.google.gson.JsonSerializer
import com.soundhub.utils.constants.Constants
import java.lang.reflect.Type
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class LocalDateTimeAdapter : JsonSerializer<LocalDateTime?>, JsonDeserializer<LocalDateTime?> {
    private val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern(Constants.DATETIME_FORMAT)

    override fun serialize(
        src: LocalDateTime?,
        typeOfSrc: Type?,
        context: JsonSerializationContext?
    ): JsonElement? = src?.format(formatter)
        ?.let { JsonPrimitive(it) }

    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): LocalDateTime? = json?.asString
        ?.let { LocalDateTime.parse(it, formatter) }
}
