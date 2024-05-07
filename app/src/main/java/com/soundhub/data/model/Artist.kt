package com.soundhub.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.google.gson.annotations.SerializedName
import com.soundhub.utils.converters.room.AlbumRoomConverter
import com.soundhub.utils.converters.room.StringListRoomConverter

@Entity
@TypeConverters(
    StringListRoomConverter::class,
    AlbumRoomConverter::class
)
data class Artist(
    @PrimaryKey
    var id: Int = 0,
    var title: String? = null,
    var description: String = "",
    var genre: List<String> = emptyList(),
    var style: List<String> = emptyList(),

    @SerializedName("albums")
    var albums: List<Album> = emptyList(),
    var thumb: String? = null
)