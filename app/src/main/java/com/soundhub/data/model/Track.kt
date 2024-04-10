package com.soundhub.data.model

import com.google.gson.annotations.SerializedName
import java.util.UUID

data class Track(
    @SerializedName("id")
    val id: UUID = UUID.randomUUID(),
    val title: String = "",
    val duration: Int
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Track
        return id == other.id &&
                title == other.title &&
                duration == other.duration
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + title.hashCode()
        result = 31 * result + duration
        return result
    }

    override fun toString(): String {
        return "Track(id=$id, title='$title', duration=$duration)"
    }
}
