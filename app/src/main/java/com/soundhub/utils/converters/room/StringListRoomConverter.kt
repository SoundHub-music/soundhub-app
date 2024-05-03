package com.soundhub.utils.converters.room

import androidx.room.TypeConverter

interface StringListRoomConverter {
    @TypeConverter
    fun toStringList(list: List<String>): String {
        return list.joinToString(",")
    }

    @TypeConverter
    fun fromStringList(stringList: String): List<String> {
        return stringList.split(",")
    }
}