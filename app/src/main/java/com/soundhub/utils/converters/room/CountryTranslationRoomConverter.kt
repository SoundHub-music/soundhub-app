package com.soundhub.utils.converters.room

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.soundhub.data.model.CountryTranslation
import java.lang.reflect.Type

interface CountryTranslationRoomConverter {
    private val ctType: Type
        get() = object : TypeToken<CountryTranslation>() {}.type

    @TypeConverter
    fun toCountryTranslationJson(countryTranslation: CountryTranslation): String =
        Gson().toJson(countryTranslation)

    @TypeConverter
    fun fromCountryTranslationJson(json: String): CountryTranslation =
        Gson().fromJson(json, ctType)
}
