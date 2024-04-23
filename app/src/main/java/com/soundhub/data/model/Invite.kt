package com.soundhub.data.model

import com.google.gson.annotations.SerializedName
import com.soundhub.data.enums.InviteStatus
import java.time.LocalDateTime
import java.util.UUID

data class Invite(
    val id: UUID = UUID.randomUUID(),

    @SerializedName("createdDateTime")
    val createdDateTime: LocalDateTime = LocalDateTime.now(),

    @SerializedName("sender")
    val sender: User,

    @SerializedName("recipient")
    val recipient: User,

    @SerializedName("status")
    val status: InviteStatus = InviteStatus.CONSIDERED
)
