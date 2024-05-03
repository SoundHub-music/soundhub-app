package com.soundhub.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import java.util.UUID

@Entity
data class Genre(
    @PrimaryKey
    @SerializedName("id")
    var id: UUID = UUID.randomUUID(),
    var name: String? = null,
    var pictureUrl: String? = null
)