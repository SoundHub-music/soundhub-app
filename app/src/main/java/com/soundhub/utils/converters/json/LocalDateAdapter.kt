package com.soundhub.utils.converters.json

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonSerializationContext
import com.google.gson.JsonSerializer
import com.soundhub.utils.constants.Constants
import java.lang.reflect.Type
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class LocalDateAdapter : JsonSerializer<LocalDate?>, JsonDeserializer<LocalDate?> {

    private val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern(Constants.DATE_FORMAT)

    override fun serialize(
        src: LocalDate?,
        typeOfSrc: Type?,
        context: JsonSerializationContext?
    ): JsonElement? {
        return src?.format(formatter)?.let { context?.serialize(it) }
    }

    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): LocalDate? {
        return json?.asString?.let { LocalDate.parse(it, formatter) }
    }
}
