package com.soundhub.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

@Entity
data class User(
    val email: String,
    val name: String = "",
    @PrimaryKey val id: String = UUID.randomUUID().toString()
)
