package com.soundhub.utils.converters.room

import androidx.room.TypeConverter

class StringMutableListRoomConverter {
    @TypeConverter
    fun mutableListToString(list: MutableList<String>): String =
        list.joinToString(",")

    @TypeConverter
    fun fromStringToMutableList(stringList: String): MutableList<String> =
        stringList.split(",").toMutableList()
}