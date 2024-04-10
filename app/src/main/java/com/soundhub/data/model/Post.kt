package com.soundhub.data.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable
import java.time.LocalDateTime
import java.util.UUID

data class Post(
    @SerializedName("id")
    val id: UUID = UUID.randomUUID(),

    @SerializedName("author")
    val author: User?,

    @SerializedName("publishDate")
    val publishDate: LocalDateTime = LocalDateTime.now(),
    val content: String = "",

    @SerializedName("imageContent")
    val images: List<String>? = emptyList(),

    @SerializedName("likes")
    var likes: Set<User> = emptySet()
): Serializable {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Post

        return id == other.id &&
                author == other.author &&
                publishDate == other.publishDate &&
                content == other.content &&
                images == other.images &&
                likes == other.likes
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + (author?.hashCode() ?: 0)
        result = 31 * result + publishDate.hashCode()
        result = 31 * result + content.hashCode()
        result = 31 * result + (images?.hashCode() ?: 0)
        result = 31 * result + likes.hashCode()
        return result
    }

    override fun toString(): String {
        return "Post(id=$id, author=$author, publishDate=$publishDate, content='$content', images=$images, likes=$likes)"
    }
}
