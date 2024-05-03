package com.soundhub.utils.converters.room

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.soundhub.data.model.CountryTranslationBody
import java.lang.reflect.Type

interface CountryTranslationBodyRoomConverter {
    private val ctbJsonType: Type
        get() = object : TypeToken<CountryTranslationBody>(){}.type

    @TypeConverter
    fun toCtbJson(ctb: CountryTranslationBody): String =
        Gson().toJson(ctb)

    @TypeConverter
    fun fromCtbJson(json: String): CountryTranslationBody =
        Gson().fromJson(json, ctbJsonType)
}