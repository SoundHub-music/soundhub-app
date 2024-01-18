package com.soundhub.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

@Entity
data class User(
    @PrimaryKey
    val id: UUID = UUID.randomUUID(),

    @ColumnInfo("email")
    val email: String,

    @ColumnInfo("first_name")
    val firstName: String = "",

    @ColumnInfo("last_name")
    val lastName: String = "",

    @ColumnInfo("country")
    val country: String = "",

    @ColumnInfo("city")
    val city: String = "",

    @ColumnInfo("password")
    val password: String? = null,

    @ColumnInfo("languages")
    val languages: List<String> = emptyList(),

    @ColumnInfo("description")
    val description: String = "",

    @ColumnInfo("session_token")
    val token: String? = null,
)