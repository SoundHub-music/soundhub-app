package com.soundhub.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import java.io.Serializable
import java.util.UUID

@Entity
data class Genre(
    @PrimaryKey
    @SerializedName("id")
    override var id: UUID = UUID.randomUUID(),
    override var name: String? = null,

    @SerializedName("pictureUrl")
    override var cover: String? = null
): Serializable, MusicEntity<UUID>