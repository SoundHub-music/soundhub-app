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
    var name: String? = null,
    var description: String = "",
    var genres: List<String> = emptyList(),
    var styles: List<String> = emptyList(),

    @SerializedName("albums")
    var albums: List<Album> = emptyList(),
    var thumbnailUrl: String? = null
)