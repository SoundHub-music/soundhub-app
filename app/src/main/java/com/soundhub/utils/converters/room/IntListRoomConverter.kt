package com.soundhub.utils.converters.room

import androidx.room.TypeConverter

class IntListRoomConverter {
    @TypeConverter
    fun toStringIntList(list: List<Int>): String =
        list.joinToString { "," }

    @TypeConverter
    fun fromStringIntList(stringList: String): List<Int> =
        stringList.split(",").map { it.toInt() }
}