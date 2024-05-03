package com.soundhub.utils.converters.room

import androidx.room.TypeConverter
import java.io.File

interface FileRoomConverter {
    @TypeConverter
    fun toStringFilePath(file: File): String = file.absolutePath

    @TypeConverter
    fun fromStringFilePath(path: String): File = File(path)
}