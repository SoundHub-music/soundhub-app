package com.soundhub.utils.converters.room

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import com.soundhub.data.model.Genre
import com.soundhub.utils.converters.json.LocalDateAdapter
import java.lang.reflect.Type
import java.time.LocalDate

class GenreRoomConverter {
    private val gson: Gson
        get() = GsonBuilder()
            .registerTypeAdapter(LocalDate::class.java, LocalDateAdapter())
            .create()

    private val genreListType: Type
        get() = object : TypeToken<List<Genre>>(){}.type

    private val genreType: Type
        get() = object : TypeToken<Genre>(){}.type

    @TypeConverter
    fun toStringGenre(genreList: Genre): String {
        return gson.toJson(genreList)
    }

    @TypeConverter
    fun fromStringGenre(genreJson: String): Genre {
        return gson.fromJson(genreJson, genreListType)
    }

    @TypeConverter
    fun toStringGenreList(genreList: List<Genre>): String {
        return gson.toJson(genreList)
    }

    @TypeConverter
    fun fromStringGenreList(genreJson: String): List<Genre> {
        return gson.fromJson(genreJson, genreListType)
    }
}